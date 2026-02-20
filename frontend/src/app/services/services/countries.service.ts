import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiClient } from '../../core/api/api.client';
import { PageResponse } from '../../core/api/api.types';
import { CountryCreateRequest, CountryUpdateRequest } from '../../interfaces/request/country.request';
import { CountryResponse } from '../../interfaces/response/country.response';

@Injectable({ providedIn: 'root' })
export class CountriesService {
  private baseUrl = '/api/countries/search';
  constructor(private api: ApiClient) {}

  search(params: {
    q?: string;
    status?: string;
    page?: number;
    size?: number;
  } = {}): Observable<PageResponse<CountryResponse>> {
    let httpParams = new HttpParams();

    if (params.q) httpParams = httpParams.set('q', params.q);
    if (params.status) httpParams = httpParams.set('status', params.status);
    httpParams = httpParams.set('page', (params.page ?? 0).toString());
    httpParams = httpParams.set('size', (params.size ?? 10).toString());

    return this.api.get<PageResponse<CountryResponse>>(this.baseUrl, { params: httpParams });
  }
/*
  list(page: number, size: number) {
    return this.api.get<PageResponse<CountryResponse>>('/api/countries', { page, size });
  }*/

  list(page: number = 0, size: number = 10): Observable<PageResponse<CountryResponse>> {
    return this.search({ page, size }); 
  }

  getById(id: number) {
    return this.api.get<CountryResponse>(`/api/countries/${id}`);
  }

  create(req: CountryCreateRequest) {
    return this.api.post<CountryResponse>('/api/countries', req);
  }

  update(id: number, req: CountryUpdateRequest) {
    return this.api.put<CountryResponse>(`/api/countries/${id}`, req);
  }

  delete(id: number) {
    return this.api.delete(`/api/countries/${id}`);
  }
}
