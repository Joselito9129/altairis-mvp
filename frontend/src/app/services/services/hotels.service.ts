import { Injectable } from '@angular/core';
import { ApiClient } from '../../core/api/api.client';
import { PageResponse } from '../../core/api/api.types';
import { HotelCreateRequest, HotelUpdateRequest } from '../../interfaces/request/hotel.request';
import { HotelResponse } from '../../interfaces/response/hotel.response';

@Injectable({ providedIn: 'root' })
export class HotelsService {
  constructor(private api: ApiClient) {}

  search(q: string | null, cityId: number | null, status: string | null, page: number, size: number) {
    return this.api.get<PageResponse<HotelResponse>>('/api/hotels', {
      q: q ?? undefined,
      cityId: cityId ?? undefined,
      status: status ?? undefined,
      page,
      size,
    });
  }

  getById(id: number) {
    return this.api.get<HotelResponse>(`/api/hotels/${id}`);
  }

  create(req: HotelCreateRequest) {
    return this.api.post<HotelResponse>('/api/hotels', req);
  }

  update(id: number, req: HotelUpdateRequest) {
    return this.api.put<HotelResponse>(`/api/hotels/${id}`, req);
  }

  delete(id: number) {
    return this.api.delete<HotelResponse>(`/api/hotels/${id}`);
  }
}
