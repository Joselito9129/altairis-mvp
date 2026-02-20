import { AfterViewInit, Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import * as L from 'leaflet';
import { HotelResponse } from '../../interfaces/response/hotel.response';

@Component({
  standalone: true,
  selector: 'app-hotels-map',
  template: `<div class="map" id="hotelsMap"></div>`,
  styles: [`
    .map { height: 520px; width: 100%; border-radius: 18px; overflow: hidden; }
  `]
})
export class HotelsMapComponent implements AfterViewInit, OnChanges {
  @Input() hotels: HotelResponse[] = [];

  private map!: L.Map;
  private markersLayer = L.layerGroup();

  ngAfterViewInit(): void {
    this.map = L.map('hotelsMap', { zoomControl: true }).setView([40.4168, -3.7038], 5); // default Spain
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap',
    }).addTo(this.map);

    this.markersLayer.addTo(this.map);
    this.renderMarkers();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['hotels'] && this.map) this.renderMarkers();
  }

  private renderMarkers() {
    this.markersLayer.clearLayers();

    const points: [number, number][] = [];

    for (const h of this.hotels || []) {
      const lat = Number((h as any).latitude);
      const lng = Number((h as any).longitude);
      if (!Number.isFinite(lat) || !Number.isFinite(lng)) continue;

      points.push([lat, lng]);

      L.marker([lat, lng])
        .bindPopup(`<b>${h.name}</b><br/>${h.code}`)
        .addTo(this.markersLayer);
    }

    if (points.length) {
      const bounds = L.latLngBounds(points);
      this.map.fitBounds(bounds.pad(0.2));
    }
  }
}
