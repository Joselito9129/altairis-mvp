import { Component, ViewChild, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HotelCreateRequest } from '../../interfaces/request/hotel.request';
import { CityResponse } from '../../interfaces/response/city.response';
import { HotelResponse } from '../../interfaces/response/hotel.response';
import { CitiesService } from '../../services/services/cities.service';
import { HotelsService } from '../../services/services/hotels.service';
import { HotelsMapComponent } from "../../shared/map/hotels-map.component";
import { MATERIAL_BASE_IMPORTS } from '../../shared/material.imports';


@Component({
  standalone: true,
  selector: 'app-hotels',
  imports: [...MATERIAL_BASE_IMPORTS, HotelsMapComponent],
  templateUrl: './hotels.component.html',
  styleUrl: './hotels.component.scss',
})
export class HotelsComponent {
  private fb = inject(FormBuilder);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  cities: CityResponse[] = [];
  data: HotelResponse[] = [];
  total = 0;
  loading = false;

  q = '';
  filterCityId: number | null = null;
  filterStatus: string | null = null;

  form = this.fb.group({
    code: ['', [Validators.required, Validators.maxLength(32)]],
    name: ['', [Validators.required, Validators.maxLength(200)]],
    cityId: [null as any, [Validators.required]],
    address: [''],
    stars: [null as any],
    latitude: [null as any],
    longitude: [null as any],
    status: ['A', [Validators.required, Validators.maxLength(1)]],
  });

  pageIndex = 0;
  pageSize = 10;

  constructor(
    private svc: HotelsService,
    private citiesSvc: CitiesService,
    private snack: MatSnackBar
  ) {
    this.loadCities();
    this.load();
  }

  loadCities() {
    this.citiesSvc.list(null, 0, 200).subscribe({
      next: (p) => (this.cities = p.content),
      error: (e) => this.fail(e),
    });
  }

  load() {
    this.loading = true;
    this.svc.search(this.q, this.filterCityId, this.filterStatus, this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content;
        this.total = p.totalElements;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }

  applyFilters() {
    this.pageIndex = 0;
    this.load();
  }

  editingId: number | null = null;

edit(row: any) {
  this.editingId = row.id;
  this.form.patchValue({
    code: row.code,
    name: row.name,
    cityId: row.cityId,
    address: row.address,
    stars: row.stars,
    latitude: row.latitude,
    longitude: row.longitude,
    status: row.status,
  });
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

cancelEdit() {
  this.editingId = null;
  this.form.reset({ status: 'A' });
}


remove(row: any) {
  if (!row?.id) return;

  const ok = confirm(`Delete hotel "${row.name}"?`);
  if (!ok) return;

  this.svc.delete(row.id).subscribe({
    next: () => {
      if (this.editingId === row.id) {
        this.cancelEdit();
      }
      this.load();
    },
    error: (e: any) => this.fail(e),
  });
}

/*
  save() {
    if (this.form.invalid) return;
    this.svc.create(this.form.getRawValue() as any).subscribe({
      next: () => {
        this.snack.open('Hotel created', 'OK', { duration: 1500 });
        this.form.reset({ status: 'A' } as any);
        this.pageIndex = 0;
        this.load();
      },
      error: (e) => this.fail(e),
    });
  }
  */

  save() {
    const v = this.form.getRawValue();
  
    if (!v.code || !v.name || !v.cityId) return;
  
    const payload: HotelCreateRequest = {
      code: v.code,
      name: v.name,
      cityId: v.cityId,
      address: v.address ?? undefined,
      stars: v.stars ?? undefined,
      latitude: v.latitude ?? undefined,
      longitude: v.longitude ?? undefined,
      status: v.status ?? 'A',
    };
  
    if (this.editingId) {
      this.svc.update(this.editingId, payload).subscribe({
        next: () => { this.cancelEdit(); this.load(); },
        error: (e) => this.fail(e),
      });
    } else {
      this.svc.create(payload).subscribe({
        next: () => { this.load(); },
        error: (e) => this.fail(e),
      });
    }
  }
  
  onPage(ev: PageEvent) {
    this.pageIndex = ev.pageIndex;
    this.pageSize = ev.pageSize;
    this.load();
  }

  private fail(e: any) {
    this.loading = false;
    this.snack.open(e?.message || 'Error', 'OK', { duration: 2500 });
  }

}
