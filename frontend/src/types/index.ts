export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  avatarUrl?: string;
  bio?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  firstName?: string;
  lastName?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  user: User;
}

export interface Trip {
  id: number;
  name: string;
  description?: string;
  location?: string;
  startDate?: string;
  endDate?: string;
  isPublic: boolean;
  shareToken: string;
  active: boolean;
  estimatedBudget?: number;
  currency: string;
  createdAt: string;
  updatedAt: string;
  creator: User;
  members: TripMember[];
}

export interface TripMember {
  id: number;
  role: 'CREATOR' | 'ADMIN' | 'MEMBER';
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED';
  joinedAt: string;
  user: User;
}

export interface Group {
  id: number;
  name: string;
  description?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
  creator: User;
  members: GroupMember[];
}

export interface GroupMember {
  id: number;
  role: 'CREATOR' | 'ADMIN' | 'MEMBER';
  joinedAt: string;
  user: User;
}

export interface Friend {
  id: number;
  status: 'PENDING' | 'ACCEPTED' | 'DECLINED' | 'BLOCKED';
  createdAt: string;
  acceptedAt?: string;
  requester: User;
  addressee: User;
}

export interface Availability {
  id: number;
  startDate: string;
  endDate: string;
  notes?: string;
  createdAt: string;
  user: User;
}

export interface Message {
  id: number;
  content: string;
  messageType: 'TEXT' | 'IMAGE' | 'FILE' | 'SYSTEM' | 'NOTIFICATION';
  edited: boolean;
  editedAt?: string;
  createdAt: string;
  sender: User;
  recipient?: User;
}

export interface Notification {
  id: number;
  type: 'FRIEND_REQUEST' | 'FRIEND_ACCEPTED' | 'TRIP_INVITATION' | 'TRIP_UPDATE' | 'MESSAGE' | 'AVAILABILITY_UPDATE' | 'SYSTEM';
  title: string;
  content?: string;
  isRead: boolean;
  actionUrl?: string;
  data?: string;
  createdAt: string;
  readAt?: string;
}