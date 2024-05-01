import dayjs from 'dayjs/esm';
import { IRoomTable } from 'app/entities/room-table/room-table.model';

export interface IVehicleTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  vehicleName?: string | null;
  vehicleType?: string | null;
  vehicleId?: string | null;
  roomId?: string | null;
  ownerId?: string | null;
  vehicleFee?: number | null;
  roomTable?: IRoomTable | null;
}

export type NewVehicleTable = Omit<IVehicleTable, 'id'> & { id: null };
