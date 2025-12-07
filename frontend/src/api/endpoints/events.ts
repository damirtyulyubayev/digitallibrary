import { api } from '../client';
import type { EventItem } from '../types';

export const fetchEvents = (): Promise<EventItem[]> =>
  api.get('/api/events').then(r => r.data);

export const registerForEvent = (id: number, payload: { userId: number }): Promise<void> =>
  api.post(`/api/events/${id}/register`, payload).then(r => r.data);
