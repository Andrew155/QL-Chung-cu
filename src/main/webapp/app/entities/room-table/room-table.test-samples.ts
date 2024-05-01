import dayjs from 'dayjs/esm';

import { IRoomTable, NewRoomTable } from './room-table.model';

export const sampleWithRequiredData: IRoomTable = {
  id: 14288,
  roomId: 'sugary',
};

export const sampleWithPartialData: IRoomTable = {
  id: 31921,
  updateAt: dayjs('2024-04-20T18:25'),
  deletedAt: dayjs('2024-04-20T17:37'),
  roomId: 'while mooch shellac',
  ownTime: 'lest',
  ownerId: 'insubstantial aggressive fervently',
  ownerName: 'concerning',
};

export const sampleWithFullData: IRoomTable = {
  id: 31705,
  createAt: dayjs('2024-04-20T09:42'),
  updateAt: dayjs('2024-04-20T15:37'),
  deletedAt: dayjs('2024-04-21T00:57'),
  roomId: 'since',
  area: 'whose how amaze',
  ownTime: 'folklore',
  ownerId: 'and sire sandpaper',
  ownerName: 'statement',
  status: 'yahoo viciously',
};

export const sampleWithNewData: NewRoomTable = {
  roomId: 'track',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
