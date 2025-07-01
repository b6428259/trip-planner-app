import { create } from 'zustand';
import type { Trip } from '@/types';

interface TripState {
  trips: Trip[];
  currentTrip: Trip | null;
  isLoading: boolean;
  setTrips: (trips: Trip[]) => void;
  addTrip: (trip: Trip) => void;
  updateTrip: (trip: Trip) => void;
  removeTrip: (tripId: number) => void;
  setCurrentTrip: (trip: Trip | null) => void;
  setLoading: (isLoading: boolean) => void;
}

export const useTripStore = create<TripState>()((set) => ({
  trips: [],
  currentTrip: null,
  isLoading: false,

  setTrips: (trips: Trip[]) => {
    set({ trips });
  },

  addTrip: (trip: Trip) => {
    set((state) => ({
      trips: [...state.trips, trip],
    }));
  },

  updateTrip: (updatedTrip: Trip) => {
    set((state) => ({
      trips: state.trips.map((trip) =>
        trip.id === updatedTrip.id ? updatedTrip : trip
      ),
      currentTrip:
        state.currentTrip?.id === updatedTrip.id ? updatedTrip : state.currentTrip,
    }));
  },

  removeTrip: (tripId: number) => {
    set((state) => ({
      trips: state.trips.filter((trip) => trip.id !== tripId),
      currentTrip: state.currentTrip?.id === tripId ? null : state.currentTrip,
    }));
  },

  setCurrentTrip: (trip: Trip | null) => {
    set({ currentTrip: trip });
  },

  setLoading: (isLoading: boolean) => {
    set({ isLoading });
  },
}));