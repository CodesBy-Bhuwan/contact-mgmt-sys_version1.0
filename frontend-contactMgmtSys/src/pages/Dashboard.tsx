import React, { useCallback, useEffect, useState } from 'react';
import {
  Plus, Search, Star, Eye, EyeOff, Pencil, Trash2, X, Globe, Camera, User as UserIcon, KeyRound
} from 'lucide-react';
import { Contact, ContactInput } from '../types';
import { contactsApi, userApi } from '../services/api';
import { useAuth } from '../contexts/AuthContext';

const EMPTY: ContactInput = {
  name: '', username: '', email: '', phoneNumber: '',
  address: '', description: '', webLink: '', facebookLink: '', password: '',
};

const Dashboard: React.FC = () => {
  const { user, setUser, logout } = useAuth();
  const [contacts, setContacts] = useState<Contact[]>([]);
  const [query, setQuery] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState<Contact | null>(null);
  const [revealed, setRevealed] = useState<Record<string, string>>({});
  const [flash, setFlash] = useState('');
  const [error, setError] = useState('');
  const [profileOpen, setProfileOpen] = useState(false);

  const load = useCallback(async (q = '') => {
    try {
      setError('');
      setContacts(q.trim() ? await contactsApi.search(q) : await contactsApi.getAll());
    } catch (e: any) {
      setError(e.response?.data?.error ?? 'Failed to load contacts');
    }
  }, []);

  useEffect(() => { load(); }, [load]);

  // debounce search — fires 300ms after typing stops
  useEffect(() => {
    const t = setTimeout(() => load(query), 300);
    return () => clearTimeout(t);
  }, [query, load]);

  const say = (msg: string) => { setFlash(msg); setTimeout(() => setFlash(''), 3000); };

  const toggleFav = async (c: Contact) => {
    const updated = await contactsApi.toggleFav(c.id);
    setContacts(prev => prev.map(x => (x.id === updated.id ? updated : x)));
  };

  const reveal = async (c: Contact) => {
    if (revealed[c.id]) {           // toggle hidden again
      setRevealed(({ [c.id]: _, ...rest }) => rest);
      return;
    }
    try {
      const pw = await contactsApi.revealPassword(c.id);
      setRevealed(prev => ({ ...prev, [c.id]: pw }));
      setTimeout(() => setRevealed(({ [c.id]: _, ...rest }) => rest), 10000); // auto-hide
    } catch (e: any) {
      say(e.response?.data?.error ?? 'No password stored');
    }
  };

  const remove = async (c: Contact) => {
    if (!window.confirm(`Delete contact "${c.name}"?`)) return;
    await contactsApi.remove(c.id);
    setContacts(prev => prev.filter(x => x.id !== c.id));
    say('Contact deleted');
  };

  const uploadPic = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    try {
      setUser(await userApi.uploadPicture(file));
      say('Profile picture updated');
    } catch (err: any) {
      say(err.response?.data?.error ?? 'Upload failed');
    }
  };

  if (!user) return null;

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-8">
      <div className="max-w-6xl mx-auto px-4 space-y-6">

        {/* ---- Profile card ---- */}
        <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6 flex flex-wrap items-center gap-6">
          <div className="relative">
            {user.profilePic ? (
              <img src={user.profilePic} alt="profile" className="h-20 w-20 rounded-full object-cover" />
            ) : (
              <div className="h-20 w-20 rounded-full bg-primary-600 flex items-center justify-center">
                <UserIcon className="h-10 w-10 text-white" />
              </div>
            )}
            <label className="absolute -bottom-1 -right-1 bg-primary-600 p-2 rounded-full cursor-pointer hover:bg-primary-700">
              <Camera className="h-4 w-4 text-white" />
              <input type="file" accept="image/*" className="hidden" onChange={uploadPic} />
            </label>
          </div>
          <div className="flex-1">
            <h1 className="text-2xl font-bold text-gray-900 dark:text-white">{user.name}</h1>
            <p className="text-gray-600 dark:text-gray-300">{user.email}</p>
            <p className="text-sm text-gray-500">{user.phoneNumber ?? 'No phone'} · via {user.provider}</p>
          </div>
          <button onClick={() => setProfileOpen(o => !o)} className="btn-secondary px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg">
            Edit profile
          </button>
        </div>

        {profileOpen && <ProfileForm onDone={(u) => { setUser(u); setProfileOpen(false); say('Profile updated'); }} />}

        {/* ---- Flash / error ---- */}
        {flash && (
          <div className="bg-green-50 dark:bg-green-900/20 border border-green-200 text-green-700 dark:text-green-400 px-4 py-3 rounded-lg text-sm">{flash}</div>
        )}
        {error && (
          <div className="bg-red-50 dark:bg-red-900/20 border border-red-200 text-red-700 dark:text-red-400 px-4 py-3 rounded-lg text-sm">{error}</div>
        )}

        {/* ---- Search + Add ---- */}
        <div className="flex gap-3">
          <div className="relative flex-1">
            <Search className="absolute left-3 top-2.5 h-5 w-5 text-gray-400" />
            <input
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search name, email, username or phone…"
              className="w-full pl-10 pr-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-primary-600 dark:bg-gray-800 dark:text-white"
            />
          </div>
          <button
            onClick={() => { setEditing(null); setModalOpen(true); }}
            className="btn-primary flex items-center gap-2 px-4"
          >
            <Plus className="h-5 w-5" /> Add Contact
          </button>
        </div>

        {/* ---- Contact grid ---- */}
        <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-4">
          {contacts.map((c) => (
            <div key={c.id} className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-5 space-y-2">
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="font-semibold text-gray-900 dark:text-white">{c.name}</h3>
                  {c.username && <p className="text-sm text-gray-500">@{c.username}</p>}
                </div>
                <button onClick={() => toggleFav(c)} aria-label="favourite">
                  <Star className={`h-5 w-5 ${c.fav ? 'text-yellow-400 fill-yellow-400' : 'text-gray-300'}`} />
                </button>
              </div>
              <p className="text-sm text-gray-600 dark:text-gray-300">{c.email}</p>
              {c.phoneNumber && <p className="text-sm text-gray-600 dark:text-gray-300">{c.phoneNumber}</p>}
              {c.webLink && (
                <a href={c.webLink} target="_blank" rel="noreferrer" className="text-sm text-primary-600 flex items-center gap-1">
                  <Globe className="h-4 w-4" /> {c.webLink}
                </a>
              )}

              {c.hasPassword && (
                <button onClick={() => reveal(c)} className="text-sm text-primary-600 flex items-center gap-1">
                  {revealed[c.id]
                    ? <><EyeOff className="h-4 w-4" /> {revealed[c.id]}</>
                    : <><Eye className="h-4 w-4" /> Show password</>}
                </button>
              )}

              <div className="flex gap-2 pt-2">
                <button onClick={() => { setEditing(c); setModalOpen(true); }}
                  className="flex items-center gap-1 text-sm px-3 py-1 border border-gray-300 dark:border-gray-600 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700 text-gray-700 dark:text-gray-200">
                  <Pencil className="h-4 w-4" /> Edit
                </button>
                <button onClick={() => remove(c)}
                  className="flex items-center gap-1 text-sm px-3 py-1 border border-red-300 text-red-600 rounded-lg hover:bg-red-50">
                  <Trash2 className="h-4 w-4" /> Delete
                </button>
              </div>
            </div>
          ))}
        </div>
        {!contacts.length && !error && (
          <p className="text-center text-gray-500 py-12">No contacts yet — add your first one!</p>
        )}
      </div>

      {/* ---- Add/Edit modal ---- */}
      {modalOpen && (
        <ContactModal
          editing={editing}
          onClose={() => setModalOpen(false)}
          onSaved={async (msg) => { setModalOpen(false); await load(query); say(msg); }}
        />
      )}
    </div>
  );
};

/* ---- Modal ---- */
const ContactModal: React.FC<{
  editing: Contact | null;
  onClose: () => void;
  onSaved: (msg: string) => void;
}> = ({ editing, onClose, onSaved }) => {
  const [form, setForm] = useState<ContactInput>(
    editing
      ? {
          name: editing.name, username: editing.username ?? '', email: editing.email,
          phoneNumber: editing.phoneNumber ?? '', address: editing.address ?? '',
          description: editing.description ?? '', webLink: editing.webLink ?? '',
          facebookLink: editing.facebookLink ?? '', password: '', // blank = keep stored
        }
      : EMPTY
  );
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [busy, setBusy] = useState(false);

  const set = (k: keyof ContactInput) => (e: any) => setForm({ ...form, [k]: e.target.value });

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    setBusy(true); setErrors({});
    try {
      if (editing) { await contactsApi.update(editing.id, form); onSaved('Contact updated'); }
      else { await contactsApi.create(form); onSaved('Contact added'); }
    } catch (err: any) {
      if (err.response?.status === 400) setErrors(err.response.data);
      else setErrors({ form: err.response?.data?.error ?? 'Something went wrong' });
    } finally { setBusy(false); }
  };

  const Err = ({ k }: { k: string }) =>
    errors[k] ? <p className="mt-1 text-sm text-red-600">{errors[k]}</p> : null;
  const inputCls = (k: string) =>
    `w-full px-3 py-2 border ${errors[k] ? 'border-red-500' : 'border-gray-300 dark:border-gray-600'} rounded-lg focus:ring-2 focus:ring-primary-600 dark:bg-gray-700 dark:text-white`;

  return (
    <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50 p-4" onClick={onClose}>
      <div className="bg-white dark:bg-gray-800 rounded-xl w-full max-w-lg p-6 space-y-4 max-h-[90vh] overflow-y-auto"
           onClick={(e) => e.stopPropagation()}>
        <div className="flex justify-between items-center">
          <h2 className="text-xl font-bold text-gray-900 dark:text-white">
            {editing ? 'Edit Contact' : 'Add Contact'}
          </h2>
          <button onClick={onClose}><X className="h-5 w-5 text-gray-500" /></button>
        </div>

        {errors.form && <p className="text-sm text-red-600">{errors.form}</p>}

        <form onSubmit={submit} className="space-y-3">
          <div>
            <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Name *</label>
            <input value={form.name} onChange={set('name')} className={inputCls('name')} />
            <Err k="name" />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Username</label>
              <input value={form.username} onChange={set('username')} className={inputCls('username')} placeholder="their login id" />
            </div>
            <div>
              <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Phone</label>
              <input value={form.phoneNumber} onChange={set('phoneNumber')} className={inputCls('phoneNumber')} />
              <Err k="phoneNumber" />
            </div>
          </div>
          <div>
            <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Email *</label>
            <input value={form.email} onChange={set('email')} className={inputCls('email')} />
            <Err k="email" />
          </div>
          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Web link</label>
              <input value={form.webLink} onChange={set('webLink')} className={inputCls('webLink')} placeholder="https://" />
            </div>
            <div>
              <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Facebook</label>
              <input value={form.facebookLink} onChange={set('facebookLink')} className={inputCls('facebookLink')} />
            </div>
          </div>
          <div>
            <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Address</label>
            <input value={form.address} onChange={set('address')} className={inputCls('address')} />
          </div>
          <div>
            <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1">Notes</label>
            <textarea rows={3} value={form.description} onChange={set('description')} className={`${inputCls('description')} resize-none`} />
          </div>
          <div>
            <label className="block text-sm text-gray-700 dark:text-gray-300 mb-1 flex items-center gap-1">
              <KeyRound className="h-4 w-4" /> Password
            </label>
            <input type="text" value={form.password} onChange={set('password')} className={inputCls('password')}
              placeholder={editing?.hasPassword ? 'Leave blank to keep the stored password' : 'Store a password (encrypted)'} />
            <Err k="password" />
          </div>
          <button type="submit" disabled={busy} className="btn-primary w-full py-2">
            {busy ? 'Saving…' : editing ? 'Save changes' : 'Add contact'}
          </button>
        </form>
      </div>
    </div>
  );
};

/* ---- Inline profile edit ---- */
const ProfileForm: React.FC<{ onDone: (u: any) => void }> = ({ onDone }) => {
  const { user } = useAuth();
  const [form, setForm] = useState({
    name: user?.name ?? '', phoneNumber: user?.phoneNumber ?? '', about: user?.about ?? '',
  });
  return (
    <div className="bg-white dark:bg-gray-800 rounded-xl shadow-sm p-6 space-y-3">
      <ProfileFields form={form} setForm={setForm} onDone={onDone} />
    </div>
  );
};

const ProfileFields: React.FC<{ form: any; setForm: any; onDone: (u: any) => void }> = ({ form, setForm, onDone }) => {
  const save = async (e: React.FormEvent) => {
    e.preventDefault();
    onDone(await userApi.updateMe(form));
  };
  const cls = 'w-full px-3 py-2 border border-gray-300 dark:border-gray-600 rounded-lg dark:bg-gray-700 dark:text-white';
  return (
    <form onSubmit={save} className="space-y-3">
      <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} className={cls} placeholder="Name" />
      <input value={form.phoneNumber} onChange={(e) => setForm({ ...form, phoneNumber: e.target.value })} className={cls} placeholder="Phone" />
      <textarea value={form.about} onChange={(e) => setForm({ ...form, about: e.target.value })} className={cls} placeholder="About you" rows={2} />
      <button className="btn-primary px-4 py-2 rounded-lg">Save profile</button>
    </form>
  );
};

export default Dashboard;