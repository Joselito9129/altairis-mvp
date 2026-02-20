import { Component, ViewChild, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BookingResponse } from '../../interfaces/response/booking.response';
import { HotelResponse } from '../../interfaces/response/hotel.response';
import { RoomTypeResponse } from '../../interfaces/response/room-type.response';
import { BookingsService } from '../../services/services/bookings.service';
import { HotelsService } from '../../services/services/hotels.service';
import { RoomTypesService } from '../../services/services/room-types.service';
import { MATERIAL_DATE_IMPORTS } from '../../shared/material.imports';

@Component({
  standalone: true,
  selector: 'app-bookings',
  imports: [...MATERIAL_DATE_IMPORTS],
  templateUrl: './bookings.component.html',
  styleUrl: './bookings.component.scss',
})
export class BookingsComponent {
  private fb = inject(FormBuilder);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  hotels: HotelResponse[] = [];
  roomTypes: RoomTypeResponse[] = [];
  data: BookingResponse[] = [];
  total = 0;
  loading = false;

  hotelId: number | null = null;
  status: string | null = null;

  filter = this.fb.group({
    hotelId: [null as any, Validators.required],
    from: [null as any, Validators.required],
    to: [null as any, Validators.required],
    status: [''],
  });

  form = this.fb.group({
    hotelId: [null as any, Validators.required],
    roomTypeId: [null as any, Validators.required],
    checkIn: [null as any, Validators.required],
    checkOut: [null as any, Validators.required],
    rooms: [1, [Validators.required, Validators.min(1)]],
    customerName: ['', [Validators.required, Validators.maxLength(160)]],
    notes: [''],
  });

  pageIndex = 0;
  pageSize = 10;

  constructor(
    private hotelsSvc: HotelsService,
    private roomSvc: RoomTypesService,
    private svc: BookingsService,
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

  onHotelChangeForCreate() {
    const hid = this.form.get('hotelId')!.value as number;
    if (!hid) return;
    this.roomSvc.listByHotel(hid, 0, 200).subscribe({
      next: (p) => (this.roomTypes = p.content),
      error: (e) => this.fail(e),
    });
  }

  load() {
    const v = this.filter.getRawValue();
    if (!v.hotelId || !v.from || !v.to) return;

    this.loading = true;
    this.hotelId = v.hotelId;
    this.status = (v.status || null) as any;

    this.svc.list(v.hotelId, this.status, this.toIso(v.from), this.toIso(v.to), this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content;
        this.total = p.totalElements;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }

  create() {
    const v = this.form.getRawValue();
    if (!v.hotelId || !v.roomTypeId || !v.checkIn || !v.checkOut) return;

    this.svc.create({
      hotelId: v.hotelId,
      roomTypeId: v.roomTypeId,
      checkIn: this.toIso(v.checkIn),
      checkOut: this.toIso(v.checkOut),
      rooms: v.rooms ?? 1,
      customerName: v.customerName ?? '',
      notes: v.notes ?? undefined,
    }).subscribe({
      next: () => {
        this.snack.open('Booking created', 'OK', { duration: 1500 });
        this.load();
      },
      error: (e) => this.fail(e),
    });
  }

  cancel(b: BookingResponse) {
    this.svc.cancel(b.id).subscribe({
      next: () => {
        this.snack.open('Booking cancelled', 'OK', { duration: 1500 });
        this.load();
      },
      error: (e) => this.fail(e),
    });
  }

  onPage(ev: PageEvent) {
    this.pageIndex = ev.pageIndex;
    this.pageSize = ev.pageSize;
    this.load();
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
