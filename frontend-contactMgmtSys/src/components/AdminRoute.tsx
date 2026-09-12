import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const AdminRoute: React.FC = () => {
  const { user, loading } = useAuth();

  if (loading) return <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900 text-gray-600">Loading…</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (!user.roles?.includes('ROLE_ADMIN')) return <Navigate to="/dashboard" replace />;

  return <Outlet />;
};

export default AdminRoute;