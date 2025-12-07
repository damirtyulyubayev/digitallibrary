export type Book = {
  id: number;
  title: string;
  author: string;
};

export type Reservation = {
  id: number;
  bookId: number;
  userId: number;
  status: string;
};

export type EventItem = {
  id: number;
  title: string;
  description?: string;
};

export type Review = {
  id: number;
  bookId: number;
  rating: number;
  comment: string;
};

export type LibraryPass = {
  id: number;
  userId: number;
  qrToken: string;
  status?: string;
};

export type Recommendation = {
  id: number;
  title: string;
  author?: string;
  reason?: string;
};

export type ReadingListItem = {
  id: number;
  userId: number;
  bookId: number;
};
