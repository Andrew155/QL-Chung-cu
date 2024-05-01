import dayjs from 'dayjs/esm';

export interface IRoomTable {
  id: number;
  createAt?: dayjs.Dayjs | null;
  updateAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  roomId?: string | null;
  area?: string | null;
  ownTime?: string | null;
  ownerId?: string | null;
  ownerName?: string | null;
  status?: string | null;
}

export type NewRoomTable = Omit<IRoomTable, 'id'> & { id: null };
