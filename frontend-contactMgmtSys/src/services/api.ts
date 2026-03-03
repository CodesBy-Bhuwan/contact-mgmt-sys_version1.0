import axios from 'axios';
import { Contact, CreateContactDto, UpdateContactDto, ApiResponse, AuthResponse, LoginDto, SignupDto } from '../types';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth API
export const authApi = {
  login: async (data: LoginDto): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/auth/login', data);
    return response.data.data;
  },
  
  signup: async (data: SignupDto): Promise<AuthResponse> => {
    const response = await api.post<ApiResponse<AuthResponse>>('/auth/signup', data);
    return response.data.data;
  },
  
  logout: async (): Promise<void> => {
    await api.post('/auth/logout');
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
  
  getCurrentUser: async (): Promise<any> => {
    const response = await api.get<ApiResponse<any>>('/auth/me');
    return response.data.data;
  },
};

// Contacts API
export const contactsApi = {
  getAllContacts: async (): Promise<Contact[]> => {
    const response = await api.get<ApiResponse<Contact[]>>('/contacts');
    return response.data.data;
  },
  
  getContactById: async (id: number): Promise<Contact> => {
    const response = await api.get<ApiResponse<Contact>>(`/contacts/${id}`);
    return response.data.data;
  },
  
  createContact: async (data: CreateContactDto): Promise<Contact> => {
    const response = await api.post<ApiResponse<Contact>>('/contacts', data);
    return response.data.data;
  },
  
  updateContact: async (id: number, data: UpdateContactDto): Promise<Contact> => {
    const response = await api.put<ApiResponse<Contact>>(`/contacts/${id}`, data);
    return response.data.data;
  },
  
  deleteContact: async (id: number): Promise<void> => {
    await api.delete(`/contacts/${id}`);
  },
  
  searchContacts: async (query: string): Promise<Contact[]> => {
    const response = await api.get<ApiResponse<Contact[]>>(`/contacts/search?q=${query}`);
    return response.data.data;
  },
};

export default api;