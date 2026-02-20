export interface BookingCreateRequest {
  hotelId: number;
  roomTypeId: number;
  checkIn: string;  // yyyy-MM-dd
  checkOut: string; // yyyy-MM-dd
  rooms: number;
  customerName: string;
  notes?: string;
}
