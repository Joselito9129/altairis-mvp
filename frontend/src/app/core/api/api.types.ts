export interface GenericResponse<T> {
  success: boolean;
  message?: string;
  code?: string;
  data?: T;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number; // page index
  size: number;
  first: boolean;
  last: boolean;
}
