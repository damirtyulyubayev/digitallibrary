import { api } from '../client';
import type { LibraryPass } from '../types';

export const fetchPassByUser = (userId: number): Promise<LibraryPass> =>
  api.get(`/api/pass/${userId}`).then(r => r.data);

export const scanPass = (qrToken: string): Promise<LibraryPass> =>
  api.get(`/api/pass/scan/${qrToken}`).then(r => r.data);
