import { Routes } from '@angular/router';

import { BookingsComponent } from './pages/bookings/bookings.component';
import { CitiesComponent } from './pages/cities/cities.component';
import { CountriesComponent } from './pages/countries/countries.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HotelsComponent } from './pages/hotels/hotels.component';
import { InventoryComponent } from './pages/inventory/inventory.component';
import { RoomTypesComponent } from './pages/room-types/room-types.component';

export const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'countries', component: CountriesComponent },
  { path: 'cities', component: CitiesComponent },
  { path: 'hotels', component: HotelsComponent },
  { path: 'room-types', component: RoomTypesComponent },
  { path: 'inventory', component: InventoryComponent },
  { path: 'bookings', component: BookingsComponent },
  { path: '**', redirectTo: '' },
];


