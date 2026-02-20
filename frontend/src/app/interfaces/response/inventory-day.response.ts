export interface InventoryDayResponse {
  id: number;
  roomTypeId: number;
  roomTypeCode: string;
  roomTypeName: string;

  hotelId: number;
  hotelCode: string;
  hotelName: string;

  date: string; // yyyy-MM-dd
  totalUnits: number;
  availableUnits: number;
  price?: number;
  currency: string;
  status: string;
}
