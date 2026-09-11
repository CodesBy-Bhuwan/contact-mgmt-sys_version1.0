import React from 'react';

const Footer: React.FC = () => (
  <footer className="bg-white dark:bg-gray-900 border-t dark:border-gray-800 py-6">
    <p className="text-center text-sm text-gray-500 dark:text-gray-400">
      © {new Date().getFullYear()} ContactSmartly — Manage your contacts smartly.
    </p>
  </footer>
);

export default Footer;