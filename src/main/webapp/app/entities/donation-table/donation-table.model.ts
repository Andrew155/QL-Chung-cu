import dayjs from 'dayjs/esm';
import { IRoomTable } from 'app/entities/room-table/room-table.model';

export interface IDonationTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  donationId?: string | null;
  donationType?: string | null;
  donationDesc?: string | null;
  donationMonth?: string | null;
  donationCost?: number | null;
  roomId?: string | null;
  status?: string | null;
  roomTable?: IRoomTable | null;
}

export type NewDonationTable = Omit<IDonationTable, 'id'> & { id: null };
