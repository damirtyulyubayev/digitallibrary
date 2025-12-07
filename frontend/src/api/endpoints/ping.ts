import { api } from '../client';

export const ping = (): Promise<string> => api.get('/api/ping').then(r => r.data);
