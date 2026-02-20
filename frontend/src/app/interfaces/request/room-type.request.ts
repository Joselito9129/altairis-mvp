export interface RoomTypeCreateRequest {
  hotelId: number;
  code: string;
  name: string;
  capacityAdults: number;
  capacityChildren: number;
  status: string;
}
export interface RoomTypeUpdateRequest {
  name: string;
  capacityAdults: number;
  capacityChildren: number;
  status: string;
}
