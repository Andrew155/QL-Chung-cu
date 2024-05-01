import dayjs from 'dayjs/esm';

import { IBillTable, NewBillTable } from './bill-table.model';

export const sampleWithRequiredData: IBillTable = {
  id: 24713,
  billId: 'for or till',
};

export const sampleWithPartialData: IBillTable = {
  id: 3384,
  deletedAt: dayjs('2024-04-21T03:33'),
  billType: 'passive frigid boo',
  billId: 'petrify except',
  billMonth: 'orangutan zowie inasmuch',
  roomId: 'unfit aw sedately',
  date: dayjs('2024-04-21T04:13'),
  billCost: 26844,
  customerID: 'physically',
};

export const sampleWithFullData: IBillTable = {
  id: 18582,
  createAt: dayjs('2024-04-20T23:57'),
  updateAt: dayjs('2024-04-21T02:49'),
  deletedAt: dayjs('2024-04-20T09:17'),
  billType: 'rectangular super a',
  billId: 'triumph cinema',
  billMonth: 'practical',
  billAmount: 28796,
  roomId: 'vivacious',
  date: dayjs('2024-04-20T15:22'),
  status: 'little sharply treasured',
  billCost: 7751,
  customerID: 'unhook',
};

export const sampleWithNewData: NewBillTable = {
  billId: 'duh',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
