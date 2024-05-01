import dayjs from 'dayjs/esm';
import { INotificationTable } from 'app/entities/notification-table/notification-table.model';

export interface IRequestTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  status?: string | null;
  userId?: string | null;
  title?: string | null;
  message?: string | null;
  reply?: string | null;
  note?: string | null;
  notificationTable?: INotificationTable | null;
}

export type NewRequestTable = Omit<IRequestTable, 'id'> & { id: null };
