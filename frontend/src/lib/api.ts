import axios from 'axios';
import type { 
  AuthResponse, 
  LoginRequest, 
  RegisterRequest, 
  User,
  Trip,
  Group,
  Friend,
  Availability,
  Message,
  Notification 
} from '@/types';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Auth API
export const authAPI = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', data);
    return response.data;
  },
  
  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/register', data);
    return response.data;
  },
  
  logout: async (): Promise<void> => {
    await api.post('/auth/logout');
  },
};

// Users API
export const usersAPI = {
  getCurrentUser: async (): Promise<User> => {
    const response = await api.get<User>('/users/me');
    return response.data;
  },
  
  getUserById: async (id: number): Promise<User> => {
    const response = await api.get<User>(`/users/${id}`);
    return response.data;
  },
  
  searchUsers: async (query: string): Promise<User[]> => {
    const response = await api.get<User[]>(`/users/search?q=${encodeURIComponent(query)}`);
    return response.data;
  },
  
  updateCurrentUser: async (data: Partial<User>): Promise<User> => {
    const response = await api.put<User>('/users/me', data);
    return response.data;
  },
  
  deleteCurrentUser: async (): Promise<void> => {
    await api.delete('/users/me');
  },
};

// Trips API
export const tripsAPI = {
  getAllTrips: async (): Promise<Trip[]> => {
    const response = await api.get<Trip[]>('/trips');
    return response.data;
  },
  
  getTripById: async (id: number): Promise<Trip> => {
    const response = await api.get<Trip>(`/trips/${id}`);
    return response.data;
  },
  
  createTrip: async (data: Partial<Trip>): Promise<Trip> => {
    const response = await api.post<Trip>('/trips', data);
    return response.data;
  },
  
  updateTrip: async (id: number, data: Partial<Trip>): Promise<Trip> => {
    const response = await api.put<Trip>(`/trips/${id}`, data);
    return response.data;
  },
  
  deleteTrip: async (id: number): Promise<void> => {
    await api.delete(`/trips/${id}`);
  },
  
  getTripByShareToken: async (token: string): Promise<Trip> => {
    const response = await api.get<Trip>(`/guest/trips/${token}`);
    return response.data;
  },
};

// Groups API
export const groupsAPI = {
  getAllGroups: async (): Promise<Group[]> => {
    const response = await api.get<Group[]>('/groups');
    return response.data;
  },
  
  getGroupById: async (id: number): Promise<Group> => {
    const response = await api.get<Group>(`/groups/${id}`);
    return response.data;
  },
  
  createGroup: async (data: Partial<Group>): Promise<Group> => {
    const response = await api.post<Group>('/groups', data);
    return response.data;
  },
  
  updateGroup: async (id: number, data: Partial<Group>): Promise<Group> => {
    const response = await api.put<Group>(`/groups/${id}`, data);
    return response.data;
  },
  
  deleteGroup: async (id: number): Promise<void> => {
    await api.delete(`/groups/${id}`);
  },
};

// Friends API
export const friendsAPI = {
  getFriends: async (): Promise<Friend[]> => {
    const response = await api.get<Friend[]>('/friends');
    return response.data;
  },
  
  sendFriendRequest: async (userId: number): Promise<Friend> => {
    const response = await api.post<Friend>('/friends/request', { userId });
    return response.data;
  },
  
  acceptFriendRequest: async (friendId: number): Promise<Friend> => {
    const response = await api.put<Friend>(`/friends/${friendId}/accept`);
    return response.data;
  },
  
  declineFriendRequest: async (friendId: number): Promise<void> => {
    await api.put(`/friends/${friendId}/decline`);
  },
  
  removeFriend: async (friendId: number): Promise<void> => {
    await api.delete(`/friends/${friendId}`);
  },
};

// Availability API
export const availabilityAPI = {
  getTripAvailabilities: async (tripId: number): Promise<Availability[]> => {
    const response = await api.get<Availability[]>(`/trips/${tripId}/availabilities`);
    return response.data;
  },
  
  addAvailability: async (tripId: number, data: Partial<Availability>): Promise<Availability> => {
    const response = await api.post<Availability>(`/trips/${tripId}/availabilities`, data);
    return response.data;
  },
  
  updateAvailability: async (tripId: number, availabilityId: number, data: Partial<Availability>): Promise<Availability> => {
    const response = await api.put<Availability>(`/trips/${tripId}/availabilities/${availabilityId}`, data);
    return response.data;
  },
  
  deleteAvailability: async (tripId: number, availabilityId: number): Promise<void> => {
    await api.delete(`/trips/${tripId}/availabilities/${availabilityId}`);
  },
};

// Messages API
export const messagesAPI = {
  getTripMessages: async (tripId: number): Promise<Message[]> => {
    const response = await api.get<Message[]>(`/trips/${tripId}/messages`);
    return response.data;
  },
  
  sendTripMessage: async (tripId: number, content: string): Promise<Message> => {
    const response = await api.post<Message>(`/trips/${tripId}/messages`, { content });
    return response.data;
  },
  
  getPrivateMessages: async (userId: number): Promise<Message[]> => {
    const response = await api.get<Message[]>(`/messages/private/${userId}`);
    return response.data;
  },
  
  sendPrivateMessage: async (userId: number, content: string): Promise<Message> => {
    const response = await api.post<Message>(`/messages/private/${userId}`, { content });
    return response.data;
  },
};

// Notifications API
export const notificationsAPI = {
  getNotifications: async (): Promise<Notification[]> => {
    const response = await api.get<Notification[]>('/notifications');
    return response.data;
  },
  
  markAsRead: async (notificationId: number): Promise<void> => {
    await api.put(`/notifications/${notificationId}/read`);
  },
  
  markAllAsRead: async (): Promise<void> => {
    await api.put('/notifications/read-all');
  },
  
  deleteNotification: async (notificationId: number): Promise<void> => {
    await api.delete(`/notifications/${notificationId}`);
  },
};

export default api;