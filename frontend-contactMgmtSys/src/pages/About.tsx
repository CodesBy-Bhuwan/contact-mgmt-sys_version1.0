import React from 'react';
import { Target, Heart, Users, Award } from 'lucide-react';

const About: React.FC = () => {
  const stats = [
    { label: 'Active Users', value: '10,000+' },
    { label: 'Contacts Managed', value: '1M+' },
    { label: 'Countries', value: '50+' },
    { label: 'Satisfaction', value: '99%' },
  ];

  const values = [
    {
      icon: <Target className="h-8 w-8" />,
      title: 'Our Mission',
      description: 'To simplify contact management for professionals worldwide, making networking effortless and effective.'
    },
    {
      icon: <Heart className="h-8 w-8" />,
      title: 'Our Vision',
      description: 'Create a world where every professional has the tools to build and maintain meaningful connections.'
    },
    {
      icon: <Users className="h-8 w-8" />,
      title: 'Our Team',
      description: 'A dedicated group of developers, designers, and customer success experts passionate about your success.'
    },
    {
      icon: <Award className="h-8 w-8" />,
      title: 'Our Commitment',
      description: 'We are committed to providing the best contact management experience with continuous innovation.'
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-4">
            About ContactSmartly
          </h1>
          <p className="text-xl text-gray-600 dark:text-gray-300 max-w-3xl mx-auto">
            We're on a mission to transform how professionals manage their contacts and build lasting relationships.
          </p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-2 md:grid-cols-4 gap-6 mb-16">
          {stats.map((stat, index) => (
            <div key={index} className="bg-white dark:bg-gray-800 p-6 rounded-xl shadow-sm text-center">
              <div className="text-3xl font-bold text-primary-600 dark:text-primary-400 mb-2">
                {stat.value}
              </div>
              <div className="text-gray-600 dark:text-gray-300">
                {stat.label}
              </div>
            </div>
          ))}
        </div>

        {/* Values */}
        <div className="grid md:grid-cols-2 gap-8">
          {values.map((value, index) => (
            <div key={index} className="bg-white dark:bg-gray-800 p-8 rounded-xl shadow-sm">
              <div className="text-primary-600 dark:text-primary-400 mb-4">
                {value.icon}
              </div>
              <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-2">
                {value.title}
              </h3>
              <p className="text-gray-600 dark:text-gray-300">
                {value.description}
              </p>
            </div>
          ))}
        </div>

        {/* Story */}
        <div className="mt-16 bg-white dark:bg-gray-800 p-8 rounded-xl shadow-sm">
          <h2 className="text-2xl font-bold text-gray-900 dark:text-white mb-4">
            Our Story
          </h2>
          <div className="prose dark:prose-invert max-w-none">
            <p className="text-gray-600 dark:text-gray-300 mb-4">
              Founded in 2024, ContactSmartly was born from a simple observation: professionals spend too much time managing contacts and not enough time nurturing relationships. Traditional contact management tools were either too complex or too simplistic.
            </p>
            <p className="text-gray-600 dark:text-gray-300 mb-4">
              We set out to create a solution that combines powerful features with intuitive design. Our platform helps you organize contacts intelligently, remember important details, and stay connected effortlessly.
            </p>
            <p className="text-gray-600 dark:text-gray-300">
              Today, we help thousands of professionals across 50+ countries manage their networks more effectively. And we're just getting started.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;