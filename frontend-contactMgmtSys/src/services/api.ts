import axios from 'axios';
import { Contact, LoginDto, SignupDto, User } from '../types';

import { ContactInput, AdminUser, AdminUserDetail } from '../types';

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
/*
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
*/

// ...authApi unchanged (login/signup/logout/getCurrentUser from before)...

export const contactsApi = {
  getAll: async (): Promise<Contact[]> =>
    (await api.get<Contact[]>('/contacts')).data,
  get: async (id: string): Promise<Contact> =>
    (await api.get<Contact>(`/contacts/${id}`)).data,
  create: async (data: ContactInput): Promise<Contact> =>
    (await api.post<Contact>('/contacts', data)).data,
  update: async (id: string, data: ContactInput): Promise<Contact> =>
    (await api.put<Contact>(`/contacts/${id}`, data)).data,
  remove: async (id: string): Promise<void> => {
    await api.delete(`/contacts/${id}`);
  },
  search: async (q: string): Promise<Contact[]> =>
    (await api.get<Contact[]>(`/contacts/search?q=${encodeURIComponent(q)}`)).data,
  toggleFav: async (id: string): Promise<Contact> =>
    (await api.patch<Contact>(`/contacts/${id}/fav`)).data,
  // The ONLY call that gets a decrypted password — backend returns {password}
  revealPassword: async (id: string): Promise<string> =>
    (await api.get<{ password: string }>(`/contacts/${id}/password`)).data.password,
};

export const userApi = {
  updateMe: async (data: { name?: string; phoneNumber?: string; about?: string }): Promise<User> =>
    (await api.put<User>('/users/me', data)).data,
  uploadPicture: async (file: File): Promise<User> => {
    const fd = new FormData();
    fd.append('file', file);
    return (await api.post<User>('/users/me/picture', fd, {
      headers: { 'Content-Type': 'multipart/form-data' }, // axios adds the boundary
    })).data;
  },
};

export const adminApi = {
  listUsers: async (): Promise<AdminUser[]> =>
    (await api.get<AdminUser[]>('/admin/users')).data,
  getUser: async (id: string): Promise<AdminUserDetail> =>
    (await api.get<AdminUserDetail>(`/admin/users/${id}`)).data,
  updateUser: async (id: string, data: { name?: string; email?: string; phoneNumber?: string; about?: string }): Promise<AdminUser> =>
    (await api.put<AdminUser>(`/admin/users/${id}`, data)).data,
  softDelete: async (id: string): Promise<void> => {
    await api.delete(`/admin/users/${id}`);
  },
  setEnabled: async (id: string, enabled: boolean): Promise<void> => {
    await api.put(`/admin/users/${id}/enabled`, { enabled });
  },
  resetPassword: async (id: string, password: string): Promise<void> => {
    await api.put(`/admin/users/${id}/password`, { password });
  },
};

export default api;
