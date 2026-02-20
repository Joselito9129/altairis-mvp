import { Component, ViewChild, inject } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CityResponse } from '../../interfaces/response/city.response';
import { CountryResponse } from '../../interfaces/response/country.response';
import { CitiesService } from '../../services/services/cities.service';
import { CountriesService } from '../../services/services/countries.service';
import { MATERIAL_BASE_IMPORTS } from '../../shared/material.imports';

@Component({
  standalone: true,
  selector: 'app-cities',
  imports: [...MATERIAL_BASE_IMPORTS],
  templateUrl: './cities.component.html',
  styleUrl: './cities.component.scss',
})
export class CitiesComponent {
  private fb = inject(FormBuilder);
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  countries: CountryResponse[] = [];
  data: CityResponse[] = [];
  total = 0;
  loading = false;

  filterCountryId: number | null = null;
  editingId: number | null = null;

  form = this.fb.group({
    countryId: [null as any, [Validators.required]],
    name: ['', [Validators.required, Validators.maxLength(160)]],
    status: ['A', [Validators.required, Validators.maxLength(1)]],
  });

  pageIndex = 0;
  pageSize = 10;

  constructor(
    private svc: CitiesService,
    private countriesSvc: CountriesService,
    private snack: MatSnackBar
  ) {
    this.loadCountries();
    this.load();
  }

  loadCountries() {
    this.countriesSvc.list(0, 200).subscribe({
      next: (p) => (this.countries = p.content),
      error: (e) => this.fail(e),
    });
  }

  ngOnInit() {
    this.countriesSvc.list().subscribe(r => this.countries = r.content);
    this.load();
  }

  load() {
    this.loading = true;
    console.log('Cargando ciudades con countryId:', this.filterCountryId); // ← debug
    this.svc.list(this.filterCountryId, this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.data = p.content ?? [];
        this.total = p.totalElements ?? 0;
        this.loading = false;
      },
      error: (e) => this.fail(e),
    });
  }
  
  applyFilter() {
    console.log('Filtro aplicado, nuevo countryId:', this.filterCountryId); // ← debug
    this.pageIndex = 0;
    this.load();
  }

  save() {
    if (!this.form.valid) return;
  
    const raw = this.form.getRawValue();

const payload = {
  countryId: raw.countryId,
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
      countryId: row.countryId,
      name: row.name,
      status: row.status
    });
  }

  remove(row: any) {
    if (!confirm(`Delete city "${row.name}"?`)) return;
  
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
