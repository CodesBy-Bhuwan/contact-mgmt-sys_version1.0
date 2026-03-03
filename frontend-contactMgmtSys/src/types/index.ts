// Theme type
export type Theme = 'light' | 'dark';

// Contact types matching your Java backend
export interface Contact {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  position?: string;
  address?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateContactDto {
  firstName: string;
  lastName: string;
  email: string;
  phone?: string;
  company?: string;
  position?: string;
  address?: string;
  notes?: string;
}

export interface UpdateContactDto extends Partial<CreateContactDto> {}

// API Response wrapper
export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
  timestamp: string;
}

// User types for authentication
export interface User {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
}

export interface LoginDto {
  email: string;
  password: string;
}

export interface SignupDto extends LoginDto {
  firstName: string;
  lastName: string;
  confirmPassword: string;
}

export interface AuthResponse {
  user: User;
  token: string;
}


