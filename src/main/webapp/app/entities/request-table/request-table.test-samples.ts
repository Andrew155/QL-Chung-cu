import dayjs from 'dayjs/esm';

import { IRequestTable, NewRequestTable } from './request-table.model';

export const sampleWithRequiredData: IRequestTable = {
  id: 16664,
};

export const sampleWithPartialData: IRequestTable = {
  id: 22115,
  updateAt: dayjs('2024-04-20T15:11'),
  status: 'until gee',
  message: 'frank',
};

export const sampleWithFullData: IRequestTable = {
  id: 20055,
  createAt: dayjs('2024-04-20T07:47'),
  updateAt: dayjs('2024-04-20T14:48'),
  deletedAt: dayjs('2024-04-20T06:21'),
  status: 'encouragement urgently',
  userId: 'miserable',
  title: 'zowie amidst flowery',
  message: 'furthermore wherever',
  reply: 'incidentally lawsuit mediocre',
  note: 'in yowza',
};

export const sampleWithNewData: NewRequestTable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
