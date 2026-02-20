import { Injectable } from '@angular/core';
import { ApiClient } from '../../core/api/api.client';
import { PageResponse } from '../../core/api/api.types';
import { CityCreateRequest, CityUpdateRequest } from '../../interfaces/request/city.request';
import { CityResponse } from '../../interfaces/response/city.response';

@Injectable({ providedIn: 'root' })
export class CitiesService {
  constructor(private api: ApiClient) {}

  list(countryId: number | null, page: number, size: number) {
    return this.api.get<PageResponse<CityResponse>>('/api/cities', { countryId: countryId ?? undefined, page, size });
  }

  getById(id: number) {
    return this.api.get<CityResponse>(`/api/cities/${id}`);
  }

  create(req: CityCreateRequest) {
    return this.api.post<CityResponse>('/api/cities', req);
  }

  update(id: number, req: CityUpdateRequest) {
    return this.api.put<CityResponse>(`/api/cities/${id}`, req);
  }

  delete(id: number) {
    return this.api.delete(`/api/cities/${id}`);
  }
}
