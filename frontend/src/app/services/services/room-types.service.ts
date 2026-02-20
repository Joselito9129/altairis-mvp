import { Injectable } from '@angular/core';
import { ApiClient } from '../../core/api/api.client';
import { PageResponse } from '../../core/api/api.types';
import { RoomTypeCreateRequest, RoomTypeUpdateRequest } from '../../interfaces/request/room-type.request';
import { RoomTypeResponse } from '../../interfaces/response/room-type.response';

@Injectable({ providedIn: 'root' })
export class RoomTypesService {
  constructor(private api: ApiClient) {}

  listByHotel(hotelId: number, page: number, size: number) {
    return this.api.get<PageResponse<RoomTypeResponse>>('/api/room-types', { hotelId, page, size });
  }

  getById(id: number) {
    return this.api.get<RoomTypeResponse>(`/api/room-types/${id}`);
  }

  create(req: RoomTypeCreateRequest) {
    return this.api.post<RoomTypeResponse>('/api/room-types', req);
  }

  update(id: number, req: RoomTypeUpdateRequest) {
    return this.api.put<RoomTypeResponse>(`/api/room-types/${id}`, req);
  }

  delete(id: number) {
    return this.api.delete(`/api/room-types/${id}`);
  }
}
