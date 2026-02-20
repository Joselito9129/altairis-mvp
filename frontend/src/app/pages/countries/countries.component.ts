import { Component, ViewChild, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CountryResponse } from '../../interfaces/response/country.response';
import { CountriesService } from '../../services/services/countries.service';
import { MATERIAL_BASE_IMPORTS } from '../../shared/material.imports';

@Component({
  standalone: true,
  selector: 'app-countries',
  imports: [...MATERIAL_BASE_IMPORTS],
  templateUrl: './countries.component.html',
  styleUrl: './countries.component.scss',
})
export class CountriesComponent {
  private fb = inject(FormBuilder);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  data: CountryResponse[] = [];
  total = 0;
  loading = false;
  editingId: number | null = null;

  form = this.fb.group({
    iso2: ['', [Validators.required, Validators.maxLength(2)]],
    name: ['', [Validators.required, Validators.maxLength(120)]],
    status: ['A', [Validators.required, Validators.maxLength(1)]],
  });

  pageIndex = 0;
  pageSize = 10;

  constructor(
    private svc: CountriesService,
    private snack: MatSnackBar
  ) {
    this.load();
  }

  
  load() {
    this.loading = true;
    this.svc.list(this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content ?? [];
        this.total = p.totalElements ?? 0;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }

  
/*
  load() {
    this.loading = true;
    this.svc.list(this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content;
        this.total = p.totalElements;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }*/

  save() {
    const raw = this.form.getRawValue();

    const payload = {
      iso2: (raw.iso2 ?? '').toUpperCase(),
      name: raw.name ?? '',
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
      iso2: row.iso2,
      name: row.name,
      status: row.status
    });
  }

  remove(row: any) {
    if (!confirm(`Delete country "${row.name}"?`)) return;
  
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
