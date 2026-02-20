import { Component, ViewChild, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HotelResponse } from '../../interfaces/response/hotel.response';
import { RoomTypeResponse } from '../../interfaces/response/room-type.response';
import { HotelsService } from '../../services/services/hotels.service';
import { RoomTypesService } from '../../services/services/room-types.service';
import { MATERIAL_BASE_IMPORTS } from '../../shared/material.imports';

@Component({
  standalone: true,
  selector: 'app-room-types',
 // imports: [ReactiveFormsModule, MatTableModule, FormsModule, MatCard, MatCardTitle, MatCardContent, MatFormField, MatLabel, MatOption, MatPaginator, MatProgressSpinner],
 imports: [...MATERIAL_BASE_IMPORTS],
  templateUrl: './room-types.component.html',
  styleUrl: './room-types.component.scss',
})
export class RoomTypesComponent {
  private fb = inject(FormBuilder);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  hotels: HotelResponse[] = [];
  data: RoomTypeResponse[] = [];
  total = 0;
  loading = false;

  hotelId: number | null = null;
  editingId: number | null = null;

  form = this.fb.group({
    hotelId: [null as any, [Validators.required]],
    code: ['', [Validators.required, Validators.maxLength(32)]],
    name: ['', [Validators.required, Validators.maxLength(160)]],
    capacityAdults: [2, [Validators.required, Validators.min(1)]],
    capacityChildren: [0, [Validators.required, Validators.min(0)]],
    status: ['A', [Validators.required, Validators.maxLength(1)]],
  });

  pageIndex = 0;
  pageSize = 10;

  constructor(
    private hotelsSvc: HotelsService,
    private svc: RoomTypesService,
    private snack: MatSnackBar
  ) {
    this.loadHotels();
  }

  loadHotels() {
    this.hotelsSvc.search(null, null, 'A', 0, 200).subscribe({
      next: (p) => (this.hotels = p.content),
      error: (e) => this.fail(e),
    });
  }

  load() {
    if (!this.hotelId) return;
    this.loading = true;
    this.svc.listByHotel(this.hotelId, this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content;
        this.total = p.totalElements;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }

  onHotelChange() {
    this.pageIndex = 0;
    this.load();
  }

  save() {
    const raw = this.form.getRawValue();

    const payload = {
      hotelId: raw.hotelId,
      code: raw.code ?? '',
      name: raw.name ?? '',
      capacityAdults: raw.capacityAdults ?? 0,
      capacityChildren: raw.capacityChildren ?? 0,
      status: raw.status ?? 'A'
    };
    
    const req$ = this.editingId
      ? this.svc.update(this.editingId, payload)
      : this.svc.create(payload);
    
    req$.subscribe(() => {
      this.editingId = null;
      this.form.reset({ status: 'A' });
      this.load();
    });
  }

  edit(row: any) {
    this.editingId = row.id;
  
    this.form.patchValue({
      hotelId: row.hotelId,
      code: row.code,
      name: row.name,
      capacityAdults: row.capacityAdults,
      capacityChildren: row.capacityChildren,
      status: row.status
    });
  }

  remove(row: any) {
    if (!confirm(`Delete room type "${row.name}"?`)) return;
  
    this.svc.delete(row.id).subscribe(() => {
      if (this.editingId === row.id) {
        this.editingId = null;
        this.form.reset({ status: 'A' });
      }
      this.load();
    });
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
