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
  phoneNumber?: string;
  address?: string;
  description?: string;
  picture?: string;
  fav?: boolean;
  webLink?: string;
  facebookLink?: string;
  socialLinks?: SocialLink[];
}

export type CreateContactDto = Omit<Contact, 'id' | 'socialLinks'>;
export type UpdateContactDto = Partial<CreateContactDto>;