import dayjs from 'dayjs/esm';
import { IRoomTable } from 'app/entities/room-table/room-table.model';

export interface ICitizenTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  citizenID?: string | null;
  name?: string | null;
  dob?: string | null;
  contact?: string | null;
  gender?: string | null;
  relation?: string | null;
  status?: string | null;
  familyId?: IRoomTable | null;
  roomTable?: IRoomTable | null;
}

export type NewCitizenTable = Omit<ICitizenTable, 'id'> & { id: null };
