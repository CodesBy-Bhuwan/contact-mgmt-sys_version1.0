import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, User, UserPlus, Phone } from 'lucide-react';
import { useAuth } from '../contexts/AuthContext';

const Signup: React.FC = () => {
  const navigate = useNavigate();
  const { signup } = useAuth();
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', phoneNumber: '',
    password: '', confirmPassword: '', about: ''
  });
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [loading, setLoading] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors(prev => { const next = { ...prev }; delete next[name]; return next; });
    }
  };

  const validateForm = () => {
    const v: Record<string, string> = {};
    if (!form.firstName) v.firstName = 'First name is required';
    if (!form.lastName) v.lastName = 'Last name is required';
    if (!form.email) v.email = 'Email is required';
    else if (!/\S+@\S+\.\S+/.test(form.email)) v.email = 'Email is invalid';
    if (!form.phoneNumber) v.phoneNumber = 'Phone number is required';
    else if (form.phoneNumber.length < 7 || form.phoneNumber.length > 14) v.phoneNumber = 'Invalid phone number';
    if (!form.password) v.password = 'Password is required';
    else if (form.password.length < 8) v.password = 'Password must be at least 8 characters';
    if (!form.confirmPassword) v.confirmPassword = 'Please confirm your password';
    else if (form.password !== form.confirmPassword) v.confirmPassword = 'Passwords do not match';
    if (!form.about) v.about = 'Please write something about yourself';
    return v;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const newErrors = validateForm();
    if (Object.keys(newErrors).length > 0) { setErrors(newErrors); return; }
    setLoading(true);
    setErrors({});
    try {
      // Backend contract: name/email/password/phoneNumber/about.
      // confirmPassword never leaves the browser.
      await signup({
        name: `${form.firstName} ${form.lastName}`.trim(),
        email: form.email,
        password: form.password,
        phoneNumber: form.phoneNumber,
        about: form.about,
      });
      // THE success notification — rendered by the Login page after redirect
      navigate('/login', { state: { successMessage: 'Registered successfully! Please log in.' } });
    } catch (err: any) {
      if (err.response?.status === 400) {
        // Backend sends { fieldName: message } — keys: name/email/password/phoneNumber/about
        setErrors(err.response.data);
      } else {
        setErrors({ form: 'Something went wrong. Please try again.' });
      }
    } finally {
      setLoading(false);
    }
  };

  const Err = ({ k }: { k: string }) =>
    errors[k] ? <p className="mt-1 text-sm text-red-600 dark:text-red-400">{errors[k]}</p> : null;

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white dark:bg-gray-800 p-8 rounded-xl shadow-sm">
        <div>
          <div className="flex justify-center">
            <div className="bg-primary-600 p-3 rounded-lg"><UserPlus className="h-8 w-8 text-white" /></div>
          </div>
          <h2 className="mt-4 text-center text-3xl font-bold text-gray-900 dark:text-white">Create Account</h2>
          <p className="mt-2 text-center text-gray-600 dark:text-gray-300">Join ContactSmartly and start managing your contacts</p>
        </div>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          {errors.form && (
            <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-600 dark:text-red-400 px-4 py-3 rounded-lg text-sm">
              {errors.form}
            </div>
          )}

          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">First Name</label>
                <div className="relative">
                  <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><User className="h-5 w-5 text-gray-400" /></div>
                  <input id="firstName" name="firstName" type="text" value={form.firstName} onChange={handleChange}
                    className={`block w-full pl-10 pr-3 py-2 border ${errors.firstName ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                    placeholder="John" />
                </div>
                <Err k="firstName" />
              </div>
              <div>
                <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Last Name</label>
                <input id="lastName" name="lastName" type="text" value={form.lastName} onChange={handleChange}
                  className={`block w-full px-3 py-2 border ${errors.lastName ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                  placeholder="Doe" />
                <Err k="lastName" />
              </div>
            </div>
            {/* Backend error key for the combined name */}
            <Err k="name" />

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Email Address</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><Mail className="h-5 w-5 text-gray-400" /></div>
                <input id="email" name="email" type="email" value={form.email} onChange={handleChange}
                  className={`block w-full pl-10 pr-3 py-2 border ${errors.email ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                  placeholder="you@example.com" />
              </div>
              <Err k="email" />
            </div>

            <div>
              <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Phone Number</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><Phone className="h-5 w-5 text-gray-400" /></div>
                <input id="phoneNumber" name="phoneNumber" type="tel" value={form.phoneNumber} onChange={handleChange}
                  className={`block w-full pl-10 pr-3 py-2 border ${errors.phoneNumber ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                  placeholder="9800000000" />
              </div>
              <Err k="phoneNumber" />
            </div>

            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Password</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><Lock className="h-5 w-5 text-gray-400" /></div>
                <input id="password" name="password" type="password" value={form.password} onChange={handleChange}
                  className={`block w-full pl-10 pr-3 py-2 border ${errors.password ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                  placeholder="••••••••" />
              </div>
              <Err k="password" />
            </div>

            <div>
              <label htmlFor="confirmPassword" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">Confirm Password</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><Lock className="h-5 w-5 text-gray-400" /></div>
                <input id="confirmPassword" name="confirmPassword" type="password" value={form.confirmPassword} onChange={handleChange}
                  className={`block w-full pl-10 pr-3 py-2 border ${errors.confirmPassword ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white`}
                  placeholder="••••••••" />
              </div>
              <Err k="confirmPassword" />
            </div>

            <div>
              <label htmlFor="about" className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">About You</label>
              <textarea id="about" name="about" rows={3} value={form.about} onChange={handleChange}
                className={`block w-full px-3 py-2 border ${errors.about ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent dark:bg-gray-700 dark:text-white resize-none`}
                placeholder="A short introduction..." />
              <Err k="about" />
            </div>
          </div>

          <button type="submit" disabled={loading}
            className="btn-primary w-full py-3 flex items-center justify-center space-x-2">
            {loading ? <span>Creating account...</span> : (<><UserPlus className="h-5 w-5" /><span>Sign up</span></>)}
          </button>

          <div className="text-center">
            <span className="text-gray-600 dark:text-gray-300">Already have an account? </span>
            <Link to="/login" className="font-medium text-primary-600 hover:text-primary-500 dark:text-primary-400">Sign in</Link>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Signup;