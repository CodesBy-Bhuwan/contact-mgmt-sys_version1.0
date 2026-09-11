import axios from 'axios';
import { Contact, CreateContactDto, UpdateContactDto, LoginDto, SignupDto, User } from '../types';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:5454/api';

// Backend root WITHOUT the /api suffix. Spring Security owns /authenticate and
// /do-logout directly under the root, so they can't go through baseURL.
const API_ROOT = API_URL.replace(/\/api\/?$/, '');

// Session-cookie auth: no interceptor, no Bearer token.
// withCredentials makes the browser store AND send the JSESSIONID cookie.
const api = axios.create({
  baseURL: API_URL,
  withCredentials: true,
  headers: { 'Content-Type': 'application/json' },
});

export const authApi = {
  // Spring Security form-login reads FORM PARAMETERS, not JSON, at /authenticate.
  // Success = 200 + Set-Cookie; failure = 401 { error: "invalid email or password" }.
  login: async (data: LoginDto): Promise<User> => {
    const body = new URLSearchParams({ email: data.email, password: data.password });
    await axios.post(`${API_ROOT}/authenticate`, body, {
      withCredentials: true,
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' }, // critical — overrides the JSON default
    });
    return authApi.getCurrentUser(); // profile fetch; session is now live
  },

  // 201 = created user | 400 = { fieldName: message } for validation/duplicate email
  signup: async (data: SignupDto): Promise<User> => {
    const response = await api.post('/auth/register', data);
    return response.data;          // raw DTO — not response.data.data
  },

  // Server destroys the session; cookie becomes worthless after this
  logout: async (): Promise<void> => {
    await axios.post(`${API_ROOT}/do-logout`, null, { withCredentials: true });
    localStorage.removeItem('user');
  },

  // 200 = logged in (body = user) | 401 = no session — treat 401 as "logged out"
  getCurrentUser: async (): Promise<User> => {
    const response = await api.get('/auth/me');
    return response.data;
  },
};

// Social login MUST be a full page navigation (consent screen + redirect chain — fetch can't do this).
// Backend redirects back to your React origin when done; the session cookie comes with it.
export const loginWithGoogle = () => { window.location.href = `${API_ROOT}/oauth2/authorization/google`; };
export const loginWithFacebook = () => { window.location.href = `${API_ROOT}/oauth2/authorization/facebook`; };

// Contacts — signatures kept exactly as yours. These will 401 until the
// contacts layer exists on the backend (see last section).
export const contactsApi = {
  getAllContacts: async (): Promise<Contact[]> => {
    const response = await api.get('/contacts');
    return response.data;
  },
  getContactById: async (id: string): Promise<Contact> => {
    const response = await api.get(`/contacts/${id}`);
    return response.data;
  },
  createContact: async (data: CreateContactDto): Promise<Contact> => {
    const response = await api.post('/contacts', data);
    return response.data;
  },
  updateContact: async (id: string, data: UpdateContactDto): Promise<Contact> => {
    const response = await api.put(`/contacts/${id}`, data);
    return response.data;
  },
  deleteContact: async (id: string): Promise<void> => {
    await api.delete(`/contacts/${id}`);
  },
  searchContacts: async (query: string): Promise<Contact[]> => {
    const response = await api.get(`/contacts/search?q=${encodeURIComponent(query)}`);
    return response.data;
  },
};

export default api;