import { api } from '../client';
import type { Recommendation } from '../types';

export const fetchRecommendationsForUser = (userId: number): Promise<Recommendation[]> =>
  api.get(`/api/recommendations/user/${userId}`).then(r => r.data);

export const fetchSimilarBooks = (bookId: number): Promise<Recommendation[]> =>
  api.get(`/api/recommendations/similar/${bookId}`).then(r => r.data);
