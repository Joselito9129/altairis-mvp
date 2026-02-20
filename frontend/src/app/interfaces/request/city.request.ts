export interface CityCreateRequest {
  countryId: number;
  name: string;
  status: string;
}
export interface CityUpdateRequest extends CityCreateRequest {}

