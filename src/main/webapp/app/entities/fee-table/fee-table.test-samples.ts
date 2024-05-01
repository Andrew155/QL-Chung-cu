import dayjs from 'dayjs/esm';

import { IFeeTable, NewFeeTable } from './fee-table.model';

export const sampleWithRequiredData: IFeeTable = {
  id: 12816,
  feeId: 'plus with',
};

export const sampleWithPartialData: IFeeTable = {
  id: 14586,
  deletedAt: dayjs('2024-04-21T00:54'),
  feeDesc: 'miserably timely urgently',
  status: 'heartfelt bustle',
  feeId: 'beanstalk salvage hmph',
};

export const sampleWithFullData: IFeeTable = {
  id: 12718,
  createAt: dayjs('2024-04-20T12:21'),
  updateAt: dayjs('2024-04-20T15:54'),
  deletedAt: dayjs('2024-04-20T10:40'),
  feeType: 'accuse ensue excitement',
  feeDesc: 'around gadzooks pish',
  feeMonth: 'yuck',
  feeCost: 1176,
  date: dayjs('2024-04-20T17:33'),
  status: 'after nor',
  feeId: 'immense uh-huh',
};

export const sampleWithNewData: NewFeeTable = {
  feeId: 'wisely modulo after',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
