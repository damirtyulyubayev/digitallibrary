import { api } from '../client';
import type { Review } from '../types';

export const postReview = (payload: Omit<Review, 'id'>): Promise<Review> =>
  api.post('/api/reviews', payload).then(r => r.data);

export const fetchReviewsByBook = (bookId: string | number): Promise<Review[]> =>
  api.get(`/api/reviews/book/${bookId}`).then(r => r.data);
