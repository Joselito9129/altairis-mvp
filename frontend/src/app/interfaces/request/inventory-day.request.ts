export interface InventoryDayUpsertRequest {
  id?: number;
  roomTypeId: number;
  date: string; // yyyy-MM-dd
  totalUnits: number;
  availableUnits: number;
  price?: number;
  currency: string;
  status: string;
}
