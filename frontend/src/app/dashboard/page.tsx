'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/authStore';
import { useTripStore } from '@/store/tripStore';
import { tripsAPI } from '@/lib/api';
import type { Trip } from '@/types';

export default function DashboardPage() {
  const { user, isAuthenticated, logout } = useAuthStore();
  const { trips, setTrips, setLoading } = useTripStore();
  const [isLoadingTrips, setIsLoadingTrips] = useState(true);
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      router.push('/login');
      return;
    }

    const loadTrips = async () => {
      try {
        setLoading(true);
        const userTrips = await tripsAPI.getAllTrips();
        setTrips(userTrips);
      } catch (error) {
        console.error('Failed to load trips:', error);
      } finally {
        setLoading(false);
        setIsLoadingTrips(false);
      }
    };

    loadTrips();
  }, [isAuthenticated, router, setTrips, setLoading]);

  const handleLogout = () => {
    logout();
    router.push('/');
  };

  if (!isAuthenticated || !user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation */}
      <nav className="bg-white shadow">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-semibold text-gray-900">TripPlanner</h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-gray-700">Welcome, {user.firstName || user.username}!</span>
              <button
                onClick={handleLogout}
                className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <h1 className="text-2xl font-bold text-gray-900 mb-6">Your Trips</h1>

          {/* Create Trip Button */}
          <div className="mb-6">
            <button className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors">
              Create New Trip
            </button>
          </div>

          {/* Trips Grid */}
          {isLoadingTrips ? (
            <div className="text-center py-12">
              <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
              <p className="mt-2 text-gray-500">Loading trips...</p>
            </div>
          ) : trips.length === 0 ? (
            <div className="text-center py-12">
              <svg
                className="mx-auto h-12 w-12 text-gray-400"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z"
                />
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M15 11a3 3 0 11-6 0 3 3 0 016 0z"
                />
              </svg>
              <h3 className="mt-2 text-sm font-medium text-gray-900">No trips yet</h3>
              <p className="mt-1 text-sm text-gray-500">Get started by creating your first trip.</p>
              <div className="mt-6">
                <button className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors">
                  Create Your First Trip
                </button>
              </div>
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
              {trips.map((trip: Trip) => (
                <div
                  key={trip.id}
                  className="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow cursor-pointer"
                >
                  <div className="p-6">
                    <h3 className="text-lg font-medium text-gray-900 mb-2">{trip.name}</h3>
                    {trip.description && (
                      <p className="text-sm text-gray-500 mb-4">{trip.description}</p>
                    )}
                    {trip.location && (
                      <div className="flex items-center text-sm text-gray-500 mb-2">
                        <svg className="mr-1.5 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                        </svg>
                        {trip.location}
                      </div>
                    )}
                    {trip.startDate && trip.endDate && (
                      <div className="flex items-center text-sm text-gray-500 mb-4">
                        <svg className="mr-1.5 h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        {new Date(trip.startDate).toLocaleDateString()} - {new Date(trip.endDate).toLocaleDateString()}
                      </div>
                    )}
                    <div className="flex justify-between items-center">
                      <span className="text-xs text-gray-500">
                        {trip.members.length} member{trip.members.length !== 1 ? 's' : ''}
                      </span>
                      <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${
                        trip.isPublic ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'
                      }`}>
                        {trip.isPublic ? 'Public' : 'Private'}
                      </span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}