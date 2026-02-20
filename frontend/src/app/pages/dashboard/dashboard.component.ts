import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatCard } from "@angular/material/card";
import { DashboardService } from '../../services/services/dashboard.service';
import { AreaChartComponent } from "../../shared/charts/area-chart.component";

@Component({
  standalone: true,
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
  imports: [MatCard, AreaChartComponent,CommonModule],
})
export class DashboardComponent implements OnInit{
  hotels = 0;
  inventory = 0;
  bookings = 0;

  labels: string[] = [];
  bookingsPerDay: number[] = [];

  constructor(private dashboardSvc: DashboardService) {}

  ngOnInit(): void {
    this.dashboardSvc.overview(30).subscribe({
      next: (res) => {
        const d = res.data;
        this.hotels = d.hotels ?? 0;
        this.inventory = d.inventory ?? 0;
        this.bookings = d.bookings ?? 0;
        this.labels = d.labels ?? [];
        this.bookingsPerDay = d.bookingsPerDay ?? [];
      },
      error: () => {
        // en demo: evita crashear
        this.hotels = this.inventory = this.bookings = 0;
        this.labels = [];
        this.bookingsPerDay = [];
      }
    });
  }
}
