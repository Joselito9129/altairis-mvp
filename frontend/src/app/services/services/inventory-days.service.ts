import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClient } from '../../core/api/api.client';
import { InventoryDayUpsertRequest } from '../../interfaces/request/inventory-day.request';
import { InventoryDayResponse } from '../../interfaces/response/inventory-day.response';

@Injectable({ providedIn: 'root' })
export class InventoryDaysService {
  constructor(private api: ApiClient) {}

  create(req: InventoryDayUpsertRequest): Observable<InventoryDayResponse> {
    return this.api.post<InventoryDayResponse>('/api/inventory-days', req);
  }

  update(id: number, req: InventoryDayUpsertRequest): Observable<InventoryDayResponse> {
    return this.api.put<InventoryDayResponse>(`/api/inventory-days/${id}`, req);
  }

  delete(id: number): Observable<void> {
    return this.api.delete<void>(`/api/inventory-days/${id}`);
  }

  byRoomType(roomTypeId: number, from: string, to: string) {
    return this.api.get<InventoryDayResponse[]>('/api/inventory-days/by-room-type', { roomTypeId, from, to });
  }

  byHotel(hotelId: number, from: string, to: string) {
    return this.api.get<InventoryDayResponse[]>('/api/inventory-days/by-hotel', { hotelId, from, to });
  }
}
