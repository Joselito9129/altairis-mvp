import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import Chart from 'chart.js/auto';

@Component({
  standalone: true,
  selector: 'app-area-chart',
  template: `<canvas #cv></canvas>`,
})
export class AreaChartComponent implements AfterViewInit {
  @ViewChild('cv') cv!: ElementRef<HTMLCanvasElement>;
  @Input() labels: string[] = [];
  @Input() values: number[] = [];

  ngAfterViewInit(): void {
    new Chart(this.cv.nativeElement, {
      type: 'line',
      data: {
        labels: this.labels,
        datasets: [{
          label: 'Bookings',
          data: this.values,
          fill: true,
          tension: 0.35,
        }]
      },
      options: {
        responsive: true,
        plugins: { legend: { display: false } },
        scales: { x: { grid: { display: false } }, y: { grid: { display: true } } }
      }
    });
  }
}
