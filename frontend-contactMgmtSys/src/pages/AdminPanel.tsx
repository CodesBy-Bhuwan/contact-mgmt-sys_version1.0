import React, { useEffect, useState } from 'react';
import { ShieldCheck, ShieldOff, Eye, Pencil, KeyRound, ChevronDown, ChevronUp } from 'lucide-react';
import { AdminUser, AdminUserDetail, Contact } from '../types';
import { adminApi } from '../services/api';

const AdminPanel: React.FC = () => {
  const [users, setUsers] = useState<AdminUser[]>([]);
  const [expanded, setExpanded] = useState<string | null>(null);
  const [detail, setDetail] = useState<AdminUserDetail | null>(null);
  const [error, setError] = useState('');
  const [flash, setFlash] = useState('');

  const say = (m: string) => { setFlash(m); setTimeout(() => setFlash(''), 3000); };

  const load = async () => {
    try { setUsers(await adminApi.listUsers()); }
    catch (e: any) { setError(e.response?.status === 403 ? 'Admin access required' : 'Failed to load users'); }
  };
  useEffect(() => { load(); }, []);

  const view = async (id: string) => {
    if (expanded === id) { setExpanded(null); setDetail(null); return; }
    const d = await adminApi.getUser(id);
    setDetail(d); setExpanded(id);
  };

  const edit = async (u: AdminUser) => {
    const name = window.prompt('Name', u.name); if (name === null) return;
    const email = window.prompt('Email', u.email); if (email === null) return;
    const phone = window.prompt('Phone', u.phoneNumber ?? ''); if (phone === null) return;
    await adminApi.updateUser(u.userId, { name, email, phoneNumber: phone });
    say('User updated'); load();
  };

  const toggleEnabled = async (u: AdminUser) => {
    if (!u.enabled && !window.confirm(`Soft-delete ${u.name}? They can't log in, data is kept.`)) return;
    await adminApi.setEnabled(u.userId, !u.enabled);
    say(u.enabled ? 'User disabled' : 'User restored'); load();
  };

  const resetPw = async (u: AdminUser) => {
    const pw = window.prompt(`New password for ${u.email} (min 8 chars)`);
    if (!pw) return;
    try { await adminApi.resetPassword(u.userId, pw); say('Password reset'); }
    catch (e: any) { say(e.response?.data?.error ?? 'Reset failed'); }
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="max-w-6xl mx-auto px-4 space-y-4">
        <h1 className="text-2xl font-bold text-gray-900 dark:text-white flex items-center gap-2">
          <ShieldCheck className="h-7 w-7 text-primary-600" /> Admin Panel
        </h1>
        {flash && <div className="bg-green-50 border border-green-200 text-green-700 px-4 py-2 rounded-lg text-sm">{flash}</div>}
        {error && <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-2 rounded-lg text-sm">{error}</div>}

        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm overflow-x-auto">
          <table className="w-full text-sm">
            <thead className="bg-gray-50 dark:bg-gray-700 text-left text-gray-600 dark:text-gray-300">
              <tr>
                <th className="px-4 py-3">User</th>
                <th className="px-4 py-3">Provider</th>
                <th className="px-4 py-3">Contacts</th>
                <th className="px-4 py-3">Status</th>
                <th className="px-4 py-3">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => (
                <React.Fragment key={u.userId}>
                  <tr className="border-t dark:border-gray-700 text-gray-700 dark:text-gray-200">
                    <td className="px-4 py-3">
                      <p className="font-medium">{u.name}</p>
                      <p className="text-gray-500">{u.email}</p>
                    </td>
                    <td className="px-4 py-3">{u.provider}</td>
                    <td className="px-4 py-3">{u.contactCount}</td>
                    <td className="px-4 py-3">
                      <span className={`px-2 py-0.5 rounded-full text-xs ${u.enabled
                        ? 'bg-green-100 text-green-700' : 'bg-red-100 text-red-700'}`}>
                        {u.enabled ? 'active' : 'disabled'}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <div className="flex gap-2 flex-wrap">
                        <IconBtn onClick={() => view(u.userId)} title="View contacts">
                          {expanded === u.userId ? <ChevronUp className="h-4 w-4" /> : <><Eye className="h-4 w-4" /><ChevronDown className="h-4 w-4" /></>}
                        </IconBtn>
                        <IconBtn onClick={() => edit(u)} title="Edit"><Pencil className="h-4 w-4" /></IconBtn>
                        <IconBtn onClick={() => resetPw(u)} title="Reset password"><KeyRound className="h-4 w-4" /></IconBtn>
                        <IconBtn onClick={() => toggleEnabled(u)} title={u.enabled ? 'Soft delete' : 'Restore'}>
                          {u.enabled ? <ShieldOff className="h-4 w-4 text-red-600" /> : <ShieldCheck className="h-4 w-4 text-green-600" />}
                        </IconBtn>
                      </div>
                    </td>
                  </tr>
                  {expanded === u.userId && detail && (
                    <tr className="bg-gray-50 dark:bg-gray-900/50">
                      <td colSpan={5} className="px-6 py-4">
                        <p className="font-medium text-gray-700 dark:text-gray-300 mb-2">
                          {detail.user.name}'s contacts ({detail.contacts.length}) — full visibility, no permission needed
                        </p>
                        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-2">
                          {detail.contacts.map((c: Contact) => (
                            <div key={c.id} className="bg-white dark:bg-gray-800 rounded-lg p-3 text-xs border dark:border-gray-700">
                              <p className="font-semibold">{c.name} {c.username && <span className="text-gray-400">@{c.username}</span>}</p>
                              <p className="text-gray-500">{c.email} · {c.phoneNumber ?? '—'}</p>
                              {c.webLink && <p className="text-primary-600">{c.webLink}</p>}
                            </div>
                          ))}
                          {!detail.contacts.length && <p className="text-gray-500 text-xs">No contacts.</p>}
                        </div>
                      </td>
                    </tr>
                  )}
                </React.Fragment>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

const IconBtn: React.FC<{ onClick: () => void; title: string; children: React.ReactNode }> = ({ onClick, title, children }) => (
  <button onClick={onClick} title={title}
    className="p-2 border border-gray-300 dark:border-gray-600 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 text-gray-700 dark:text-gray-200">
    {children}
  </button>
);

export default AdminPanel;