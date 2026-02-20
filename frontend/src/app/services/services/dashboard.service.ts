import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface GenericResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

import { DashboardOverviewResponse } from '../../interfaces/response/dashboard-overview.response';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  overview(days = 30): Observable<GenericResponse<DashboardOverviewResponse>> {
    return this.http.get<GenericResponse<DashboardOverviewResponse>>(
      `${this.base}/api/dashboard/overview?days=${days}`
    );
  }
}
