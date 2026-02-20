export interface BookingResponse {
  id: number;
  bookingRef: string;

  hotelId: number;
  hotelCode: string;
  hotelName: string;

  roomTypeId: number;
  roomTypeCode: string;
  roomTypeName: string;

  checkIn: string;
  checkOut: string;
  rooms: number;

  customerName: string;
  notes?: string;

  status: string; // 'C'/'X'/'P'
}
