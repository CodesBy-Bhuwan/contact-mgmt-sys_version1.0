import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import { User, SignupDto } from '../types';
import { authApi } from '../services/api';

interface AuthContextType {
  user: User | null;
  loading: boolean;
  login: (email: string, password: string) => Promise<User>;
  signup: (data: SignupDto) => Promise<User>;
  logout: () => Promise<void>;
  setUser: (u: User | null ) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  // true until the first session check finishes — without this, logged-in users
  // get bounced to /login on every page refresh
  const [loading, setLoading] = useState(true);

  // The session lives in an HttpOnly cookie JS can't read, so the ONLY way to
  // know login state is to ask /auth/me. Runs once on load = survives refresh.
  useEffect(() => {
    authApi.getCurrentUser()
      .then(setUser)
      .catch(() => setUser(null))
      .finally(() => setLoading(false));
  }, []);

  const login = useCallback(async (email: string, password: string) => {
    const u = await authApi.login({ email, password }); // sets the session cookie
    setUser(u);
    return u;
  }, []);

  const signup = useCallback(async (data: SignupDto) => authApi.signup(data), []);

  const logout = useCallback(async () => {
    try {
      await authApi.logout();
    } finally {
      setUser(null);
    }
  }, []);

  return (
    <AuthContext.Provider value={{ user, loading, login, signup, logout, setUser }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};