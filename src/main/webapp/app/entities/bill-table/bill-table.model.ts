import dayjs from 'dayjs/esm';
import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface IBillTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  billType?: string | null;
  billId?: string | null;
  billMonth?: string | null;
  billAmount?: number | null;
  roomId?: string | null;
  date?: dayjs.Dayjs | null;
  status?: string | null;
  billCost?: number | null;
  customerID?: string | null;
  roomTable?: IRoomTable | null;
  applicationTable?: IApplicationUser | null;
}

export type NewBillTable = Omit<IBillTable, 'id'> & { id: null };
