import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { APP_CONFIG } from '../../config';
import { GenericResponse } from './api.types';

@Injectable({ providedIn: 'root' })
export class ApiClient {
  private readonly base = APP_CONFIG.apiBaseUrl;

  constructor(private http: HttpClient) {}

  get<T>(path: string, params?: Record<string, any>): Observable<T> {
    return this.http
      .get<GenericResponse<T>>(`${this.base}${path}`, { params: this.toParams(params) })
      .pipe(map((r) => this.unwrap(r)));
  }

  post<T>(path: string, body?: any, params?: Record<string, any>): Observable<T> {
    return this.http
      .post<GenericResponse<T>>(`${this.base}${path}`, body ?? {}, { params: this.toParams(params) })
      .pipe(map((r) => this.unwrap(r)));
  }

  put<T>(path: string, body?: any, params?: Record<string, any>): Observable<T> {
    return this.http
      .put<GenericResponse<T>>(`${this.base}${path}`, body ?? {}, { params: this.toParams(params) })
      .pipe(map((r) => this.unwrap(r)));
  }

  delete<T>(path: string, params?: Record<string, any>): Observable<T> {
    return this.http
      .delete<GenericResponse<T>>(`${this.base}${path}`, { params: this.toParams(params) })  // ← Cambia .get por .delete
      .pipe(map((r) => this.unwrap(r)));
  }

  private unwrap<T>(resp: GenericResponse<T>): T {
    if (!resp?.success) {
      const msg = resp?.message || resp?.code || 'Request failed';
      throw new Error(msg);
    }
    return resp.data as T;
  }

  private toParams(params?: Record<string, any>): HttpParams {
    let p = new HttpParams();
    if (!params) return p;
    Object.entries(params).forEach(([k, v]) => {
      if (v === null || v === undefined || v === '') return;
      p = p.set(k, String(v));
    });
    return p;
  }
}
