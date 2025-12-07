import { api } from '../client';
import type { ReadingListItem } from '../types';

export const addToReadingList = (userId: number, payload: { bookId: number }): Promise<ReadingListItem> =>
  api.post(`/api/reading-list/${userId}/add`, payload).then(r => r.data);

export const fetchReadingList = (userId: number): Promise<ReadingListItem[]> =>
  api.get(`/api/reading-list/${userId}`).then(r => r.data);
