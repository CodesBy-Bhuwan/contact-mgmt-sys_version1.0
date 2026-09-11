import React from 'react';
import { useNavigate } from 'react-router-dom';
import { LogOut, User as UserIcon } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

const Dashboard: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  if (!user) return null; // ProtectedRoute guarantees a user, TS just needs the check

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-12">
      <div className="max-w-3xl mx-auto px-4">
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-8">
          <div className="flex items-center justify-between mb-8">
            <div className="flex items-center gap-4">
              {user.profilePic ? (
                <img src={user.profilePic} alt="profile" className="h-16 w-16 rounded-full object-cover" />
              ) : (
                <div className="h-16 w-16 rounded-full bg-primary-600 flex items-center justify-center">
                  <UserIcon className="h-8 w-8 text-white" />
                </div>
              )}
              <div>
                <h1 className="text-2xl font-bold text-gray-900 dark:text-white">Welcome, {user.name}!</h1>
                <p className="text-gray-600 dark:text-gray-300">{user.email}</p>
              </div>
            </div>
            <button onClick={handleLogout} className="btn-primary flex items-center gap-2">
              <LogOut className="h-5 w-5" /> Logout
            </button>
          </div>
          <div className="grid grid-cols-2 gap-4 text-sm text-gray-700 dark:text-gray-300">
            <p><strong>Provider:</strong> {user.provider}</p>
            <p><strong>Email verified:</strong> {user.emailVerified ? 'Yes' : 'No'}</p>
            <p><strong>Phone:</strong> {user.phoneNumber ?? '—'}</p>
            <p><strong>About:</strong> {user.about ?? '—'}</p>
          </div>
          {/* Contacts UI lands here once the /api/contacts backend batch is built */}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;