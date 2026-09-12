// Theme type
export type Theme = 'light' | 'dark';

// ---- Auth ----
// Matches backend UserDto exactly (from /auth/register and /auth/me)
export interface User {
  userId: string;
  name: string;
  email: string;
  phoneNumber: string | null;
  about: string | null;
  profilePic: string | null;
  emailVerified: boolean;
  provider: 'SELF' | 'GOOGLE' | 'FACEBOOK' | 'GITHUB';
  roles: string[]; // new role = admin gets unlocked
}

// Field names MUST match backend UserForm — it's @Validated there.
// NOTE: backend requires name, email, password, phoneNumber AND about.
export interface SignupDto {
  name: string;
  email: string;
  password: string;
  phoneNumber: string;
  about: string;
}

export interface LoginDto {
  email: string;
  password: string;
}

// ---- Contacts (matches backend Contact entity) ----
export interface SocialLink {
  id: number;
  link: string;
  title: string;
}

export interface Contact {
  id: string; // backend generates a UUID string, not a number
  name: string;
  email: string;
  username?: string | null;
  phoneNumber?: string | null;
  address?: string | null;
  description?: string | null;
  fav: boolean;
  webLink?: string | null;
  facebookLink?: string | null;
  socialLinks?: SocialLink[] | null;
  hasPassword: boolean;
}
export interface ContactInput {
  name: string;
  username?: string;
  email: string;
  phoneNumber?: string;
  address?: string;
  description?: string;
  webLink?: string;
  facebookLink?: string;
  password?: string;   // on update: blank = keep stored password
}

// ---- Admin ----
export interface AdminUser {
  userId: string;
  name: string;
  email: string;
  phoneNumber: string | null;
  about: string | null;
  profilePic: string | null;
  enabled: boolean;
  emailVerified: boolean;
  provider: string;
  roles: string[];
  contactCount: number;
}


export interface AdminUserDetail {
  user: AdminUser;
  contacts: Contact[];
}

export type CreateContactDto = Omit<Contact, 'id' | 'socialLinks'>;
export type UpdateContactDto = Partial<CreateContactDto>;