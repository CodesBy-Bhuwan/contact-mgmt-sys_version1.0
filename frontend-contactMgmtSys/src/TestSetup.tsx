import React from 'react';

const TestSetup: React.FC = () => {
  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-900 p-8">
      {/* Test Card */}
      <div className="max-w-md mx-auto bg-white dark:bg-gray-800 rounded-xl shadow-lg overflow-hidden">
        <div className="bg-gradient-to-r from-primary-600 to-primary-800 p-6">
          <h1 className="text-2xl font-bold text-white">
            React + TypeScript + Tailwind
          </h1>
        </div>
        
        <div className="p-6 space-y-4">
          {/* Test Grid */}
          <div className="grid grid-cols-3 gap-3">
            <div className="bg-blue-500 text-white p-3 rounded-lg text-center">1</div>
            <div className="bg-green-500 text-white p-3 rounded-lg text-center">2</div>
            <div className="bg-purple-500 text-white p-3 rounded-lg text-center">3</div>
          </div>
          
          {/* Test Typography */}
          <div className="space-y-2">
            <h2 className="text-xl font-semibold text-gray-900 dark:text-white">
              Tailwind Classes Test
            </h2>
            <p className="text-gray-600 dark:text-gray-300">
              If you see colors, spacing, and proper styling, 
              <span className="font-bold text-primary-600 dark:text-primary-400"> Tailwind is working!</span>
            </p>
          </div>
          
          {/* Test Button */}
          <button className="btn-primary w-full">
            Test Button
          </button>
          
          {/* Test Theme Toggle (simulated) */}
          <div className="flex items-center justify-between p-3 bg-gray-100 dark:bg-gray-700 rounded-lg">
            <span className="text-gray-700 dark:text-gray-200">Dark Mode Test</span>
            <div className="w-10 h-5 bg-gray-300 dark:bg-primary-600 rounded-full"></div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TestSetup;