import dayjs from 'dayjs/esm';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface INotificationTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  title?: string | null;
  content?: string | null;
  userID?: string | null;
  applicationTable?: IApplicationUser | null;
}

export type NewNotificationTable = Omit<INotificationTable, 'id'> & { id: null };
