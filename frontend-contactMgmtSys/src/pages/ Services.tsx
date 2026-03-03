import React from 'react';
import { 
  Phone, Mail, Calendar, Search, 
  Shield, Cloud, Zap, Users,
  CheckCircle 
} from 'lucide-react';

const Services: React.FC = () => {
  const mainServices = [
    {
      icon: <Users className="h-12 w-12" />,
      title: 'Contact Organization',
      description: 'Automatically organize contacts with smart tags, categories, and custom fields.',
      features: ['Smart tagging', 'Custom fields', 'Bulk operations', 'Contact merging']
    },
    {
      icon: <Search className="h-12 w-12" />,
      title: 'Advanced Search',
      description: 'Find any contact instantly with powerful search and filtering options.',
      features: ['Full-text search', 'Filter by tags', 'Saved searches', 'Search history']
    },
    {
      icon: <Calendar className="h-12 w-12" />,
      title: 'Interaction Tracking',
      description: 'Keep track of all interactions with your contacts in one place.',
      features: ['Call logs', 'Email history', 'Meeting notes', 'Follow-up reminders']
    },
    {
      icon: <Shield className="h-12 w-12" />,
      title: 'Data Security',
      description: 'Enterprise-grade security to protect your valuable contact data.',
      features: ['End-to-end encryption', 'Two-factor auth', 'Access control', 'Audit logs']
    },
    {
      icon: <Cloud className="h-12 w-12" />,
      title: 'Cloud Sync',
      description: 'Access your contacts from anywhere, on any device.',
      features: ['Real-time sync', 'Offline access', 'Backup & restore', 'Multi-device']
    },
    {
      icon: <Zap className="h-12 w-12" />,
      title: 'Integration',
      description: 'Connect with your favorite tools and services.',
      features: ['Email clients', 'Calendar apps', 'CRM systems', 'API access']
    }
  ];

  const plans = [
    {
      name: 'Free',
      price: '$0',
      period: 'forever',
      features: [
        'Up to 100 contacts',
        'Basic organization',
        'Search functionality',
        'Email support'
      ],
      buttonText: 'Get Started',
      popular: false
    },
    {
      name: 'Pro',
      price: '$9.99',
      period: 'per month',
      features: [
        'Unlimited contacts',
        'Advanced organization',
        'Interaction tracking',
        'Priority support',
        'API access',
        'Team collaboration'
      ],
      buttonText: 'Start Free Trial',
      popular: true
    },
    {
      name: 'Business',
      price: '$29.99',
      period: 'per month',
      features: [
        'Everything in Pro',
        'SSO integration',
        'Custom fields',
        'Audit logs',
        'Dedicated account manager',
        'SLA guarantee'
      ],
      buttonText: 'Contact Sales',
      popular: false
    }
  ];

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 py-12">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        {/* Header */}
        <div className="text-center mb-12">
          <h1 className="text-4xl font-bold text-gray-900 dark:text-white mb-4">
            Our Services
          </h1>
          <p className="text-xl text-gray-600 dark:text-gray-300 max-w-3xl mx-auto">
            Comprehensive contact management solutions designed to help you build and maintain meaningful professional relationships.
          </p>
        </div>

        {/* Main Services */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8 mb-16">
          {mainServices.map((service, index) => (
            <div key={index} className="bg-white dark:bg-gray-800 p-8 rounded-xl shadow-sm hover:shadow-md transition-shadow">
              <div className="text-primary-600 dark:text-primary-400 mb-4">
                {service.icon}
              </div>
              <h3 className="text-xl font-semibold text-gray-900 dark:text-white mb-3">
                {service.title}
              </h3>
              <p className="text-gray-600 dark:text-gray-300 mb-4">
                {service.description}
              </p>
              <ul className="space-y-2">
                {service.features.map((feature, idx) => (
                  <li key={idx} className="flex items-center text-sm text-gray-500 dark:text-gray-400">
                    <CheckCircle className="h-4 w-4 text-primary-600 dark:text-primary-400 mr-2" />
                    {feature}
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        {/* Pricing Section */}
        <div className="mt-20">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900 dark:text-white mb-4">
              Simple, Transparent Pricing
            </h2>
            <p className="text-xl text-gray-600 dark:text-gray-300">
              Choose the plan that's right for you
            </p>
          </div>

          <div className="grid md:grid-cols-3 gap-8">
            {plans.map((plan, index) => (
              <div
                key={index}
                className={`relative bg-white dark:bg-gray-800 rounded-xl shadow-sm ${
                  plan.popular ? 'ring-2 ring-primary-600 dark:ring-primary-400' : ''
                }`}
              >
                {plan.popular && (
                  <div className="absolute top-0 right-0 -translate-y-1/2 translate-x-1/2">
                    <span className="bg-primary-600 text-white px-3 py-1 rounded-full text-sm font-medium">
                      Popular
                    </span>
                  </div>
                )}
                <div className="p-8">
                  <h3 className="text-2xl font-bold text-gray-900 dark:text-white mb-2">
                    {plan.name}
                  </h3>
                  <div className="mb-4">
                    <span className="text-4xl font-bold text-gray-900 dark:text-white">
                      {plan.price}
                    </span>
                    <span className="text-gray-500 dark:text-gray-400 ml-2">
                      {plan.period}
                    </span>
                  </div>
                  <ul className="space-y-3 mb-8">
                    {plan.features.map((feature, idx) => (
                      <li key={idx} className="flex items-center text-gray-600 dark:text-gray-300">
                        <CheckCircle className="h-5 w-5 text-primary-600 dark:text-primary-400 mr-2" />
                        {feature}
                      </li>
                    ))}
                  </ul>
                  <button className={`w-full py-3 rounded-lg font-medium transition-colors ${
                    plan.popular
                      ? 'btn-primary'
                      : 'border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-200 hover:bg-gray-50 dark:hover:bg-gray-700'
                  }`}>
                    {plan.buttonText}
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Services;