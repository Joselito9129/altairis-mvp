import { Component, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { InventoryDayUpsertRequest } from '../../interfaces/request/inventory-day.request';
import { HotelResponse } from '../../interfaces/response/hotel.response';
import { InventoryDayResponse } from '../../interfaces/response/inventory-day.response';
import { RoomTypeResponse } from '../../interfaces/response/room-type.response';
import { HotelsService } from '../../services/services/hotels.service';
import { InventoryDaysService } from '../../services/services/inventory-days.service';
import { RoomTypesService } from '../../services/services/room-types.service';
import { MATERIAL_DATE_IMPORTS } from '../../shared/material.imports';

@Component({
  standalone: true,
  selector: 'app-inventory',
  imports: [...MATERIAL_DATE_IMPORTS],
  templateUrl: './inventory.component.html',
  styleUrl: './inventory.component.scss',
})
export class InventoryComponent {
  private fb = inject(FormBuilder);
  hotels: HotelResponse[] = [];
  roomTypes: RoomTypeResponse[] = [];
  rows: InventoryDayResponse[] = [];
  loading = false;

  editingId: number | null = null;  // ← nuevo: null = crear, número = editar

  form = this.fb.group({
    hotelId: [null as any, Validators.required],
    roomTypeId: [null as any, Validators.required],
    from: [null as any, Validators.required],
    to: [null as any, Validators.required],
  });

  upsert = this.fb.group({
    roomTypeId: [null as any, Validators.required],
    date: [null as any, Validators.required],
    totalUnits: [0, [Validators.required, Validators.min(0)]],
    availableUnits: [0, [Validators.required, Validators.min(0)]],
    price: [null as any],
    currency: ['EUR', [Validators.required, Validators.maxLength(3)]],
    status: ['A', [Validators.required, Validators.maxLength(1)]],
  });

  constructor(
    private hotelsSvc: HotelsService,
    private roomSvc: RoomTypesService,
    private invSvc: InventoryDaysService,
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

  onHotelChange() {
    const hotelId = this.form.get('hotelId')!.value as number;
    if (!hotelId) return;
    this.roomSvc.listByHotel(hotelId, 0, 200).subscribe({
      next: (p) => (this.roomTypes = p.content),
      error: (e) => this.fail(e),
    });
  }

  load() {
    const v = this.form.getRawValue();
    if (!v.hotelId || !v.from || !v.to) return;
    this.loading = true;

    // byHotel gives operational overview across room types
    const from = this.toIso(v.from);
    const to = this.toIso(v.to);

    this.invSvc.byHotel(v.hotelId, from, to).subscribe({
      next: (list) => {
        this.rows = list.sort((a, b) => a.date.localeCompare(b.date));
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }

  edit(row: InventoryDayResponse) {
    this.editingId = row.id;
    this.upsert.patchValue({
      roomTypeId: row.roomTypeId,
      date: row.date,
      totalUnits: row.totalUnits,
      availableUnits: row.availableUnits,
      price: row.price,
      currency: row.currency,
      status: row.status,
    });
    // Opcional: scroll al formulario
    document.querySelector('.row2')?.scrollIntoView({ behavior: 'smooth' });
  }

  cancelEdit() {
    this.editingId = null;
    this.upsert.reset({
      roomTypeId: null,
      date: null,
      totalUnits: 0,
      availableUnits: 0,
      price: null,
      currency: 'EUR',
      status: 'A',
    });
  }

  save() {
    if (this.upsert.invalid) return;
  
    const u = this.upsert.getRawValue();
  
    const payload: InventoryDayUpsertRequest = {
      id: this.editingId ?? undefined,  // solo en update
      roomTypeId: u.roomTypeId,
      date: this.toIso(u.date),
      totalUnits: u.totalUnits ?? 0,
      availableUnits: u.availableUnits ?? 0,
      price: u.price ?? undefined,
      currency: u.currency ?? 'EUR',
      status: u.status ?? 'A',
    };
  
    const req$ = this.editingId
      ? this.invSvc.update(this.editingId, payload)
      : this.invSvc.create(payload);
  
    req$.subscribe({
      next: () => {
        this.snack.open(this.editingId ? 'Inventory updated' : 'Inventory created', 'OK', { duration: 1500 });
        this.cancelEdit();
        this.load();
      },
      error: (e) => this.fail(e),
    });
  }

  remove(row: InventoryDayResponse) {
    if (!confirm(`Delete inventory for ${row.date} (${row.roomTypeCode})?`)) return;

    this.invSvc.delete(row.id).subscribe({
      next: () => {
        this.snack.open('Inventory deleted', 'OK', { duration: 1500 });
        if (this.editingId === row.id) {
          this.cancelEdit();
        }
        this.load();
      },
      error: (e) => this.fail(e),
    });
  }

  private toIso(d: any): string {
    const dt = new Date(d);
    const yyyy = dt.getFullYear();
    const mm = String(dt.getMonth() + 1).padStart(2, '0');
    const dd = String(dt.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  private fail(e: any) {
    this.loading = false;
    this.snack.open(e?.message || 'Error', 'OK', { duration: 2500 });
  }
}