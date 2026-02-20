export interface HotelResponse {
  id: number;
  code: string;
  name: string;

  cityId: number;
  cityName: string;

  countryId: number;
  countryIso2: string;
  countryName: string;

  address?: string;
  stars?: number;

  latitude?: number;
  longitude?: number;

  status: string;
}
