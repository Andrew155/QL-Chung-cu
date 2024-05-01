import dayjs from 'dayjs/esm';

import { INotificationTable, NewNotificationTable } from './notification-table.model';

export const sampleWithRequiredData: INotificationTable = {
  id: 3067,
};

export const sampleWithPartialData: INotificationTable = {
  id: 17366,
  createAt: dayjs('2024-04-21T01:00'),
  deletedAt: dayjs('2024-04-20T05:19'),
  title: 'up reproach and',
  userID: 'meh street',
};

export const sampleWithFullData: INotificationTable = {
  id: 4733,
  createAt: dayjs('2024-04-20T21:31'),
  updateAt: dayjs('2024-04-20T10:15'),
  deletedAt: dayjs('2024-04-20T21:11'),
  title: 'arrange despite bouncy',
  content: 'over',
  userID: 'surprisingly pricey',
};

export const sampleWithNewData: NewNotificationTable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
