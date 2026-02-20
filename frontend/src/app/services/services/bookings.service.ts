import { Injectable } from '@angular/core';
import { ApiClient } from '../../core/api/api.client';
import { PageResponse } from '../../core/api/api.types';
import { BookingCreateRequest } from '../../interfaces/request/booking.request';
import { BookingNightResponse } from '../../interfaces/response/booking-night.response';
import { BookingResponse } from '../../interfaces/response/booking.response';

@Injectable({ providedIn: 'root' })
export class BookingsService {
  constructor(private api: ApiClient) {}

  create(req: BookingCreateRequest) {
    return this.api.post<BookingResponse>('/api/bookings', req);
  }

  cancel(id: number) {
    return this.api.post<BookingResponse>(`/api/bookings/${id}/cancel`, {});
  }

  getById(id: number) {
    return this.api.get<BookingResponse>(`/api/bookings/${id}`);
  }

  list(hotelId: number, status: string | null, from: string, to: string, page: number, size: number) {
    return this.api.get<PageResponse<BookingResponse>>('/api/bookings', {
      hotelId,
      status: status ?? undefined,
      from,
      to,
      page,
      size,
    });
  }

  nights(id: number) {
    return this.api.get<BookingNightResponse[]>(`/api/bookings/${id}/nights`);
  }
}
