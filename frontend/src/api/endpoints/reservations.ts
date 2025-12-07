import { api } from '../client';
import type { Reservation } from '../types';

export const createReservation = (payload: { userId: number; bookId: number }): Promise<Reservation> =>
  api.post('/api/reservations', payload).then(r => r.data);

export const returnReservation = (id: number): Promise<Reservation> =>
  api.post(`/api/reservations/${id}/return`).then(r => r.data);

export const issueReservation = (id: number): Promise<Reservation> =>
  api.post(`/api/reservations/${id}/issue`).then(r => r.data);

export const fetchReservationsByUser = (userId: number): Promise<Reservation[]> =>
  api.get(`/api/reservations/user/${userId}`).then(r => r.data);
