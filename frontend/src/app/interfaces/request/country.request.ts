export interface CountryCreateRequest {
  iso2: string;
  name: string;
  status: string; // 'A'/'I'
}
export interface CountryUpdateRequest extends CountryCreateRequest {}
