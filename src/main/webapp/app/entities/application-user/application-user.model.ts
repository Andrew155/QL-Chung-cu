import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';

export interface IApplicationUser {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  familyId?: string | null;
  citizenID?: string | null;
  name?: string | null;
  dob?: string | null;
  contact?: string | null;
  gender?: string | null;
  relation?: string | null;
  status?: string | null;
  roomId?: string | null;
  internalUser?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
