import { api } from '../client';
import type { Book } from '../types';

export const fetchBooks = (q?: string): Promise<Book[]> =>
  api.get('/api/books', { params: { q } }).then(r => r.data);

export const fetchBook = (id: string | number): Promise<Book> =>
  api.get(`/api/books/${id}`).then(r => r.data);
