import dayjs from 'dayjs/esm';
import { IRoomTable } from 'app/entities/room-table/room-table.model';

export interface IFeeTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  feeType?: string | null;
  feeDesc?: string | null;
  feeMonth?: string | null;
  feeCost?: number | null;
  date?: dayjs.Dayjs | null;
  status?: string | null;
  feeId?: string | null;
  roomTable?: IRoomTable | null;
}

export type NewFeeTable = Omit<IFeeTable, 'id'> & { id: null };
