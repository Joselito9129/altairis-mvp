export interface HotelCreateRequest {
  code: string;
  name: string;
  cityId: number;
  address?: string;
  stars?: number;
  latitude?: number;
  longitude?: number;
  status: string;
}
export interface HotelUpdateRequest extends Omit<HotelCreateRequest, 'code'> {}
