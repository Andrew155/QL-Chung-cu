import dayjs from 'dayjs/esm';

import { IDonationTable, NewDonationTable } from './donation-table.model';

export const sampleWithRequiredData: IDonationTable = {
  id: 15764,
  donationId: 'excepting recess',
};

export const sampleWithPartialData: IDonationTable = {
  id: 29779,
  updateAt: dayjs('2024-04-20T10:23'),
  deletedAt: dayjs('2024-04-20T12:59'),
  donationId: 'improbable aside',
  donationType: 'lampoon drat',
  donationDesc: 'charm stable as',
  donationMonth: 'promise furthermore relinquish',
  donationCost: 24072,
};

export const sampleWithFullData: IDonationTable = {
  id: 25757,
  createAt: dayjs('2024-04-20T17:13'),
  updateAt: dayjs('2024-04-21T03:01'),
  deletedAt: dayjs('2024-04-20T12:55'),
  donationId: 'times till',
  donationType: 'optimistically lighten judge',
  donationDesc: 'like',
  donationMonth: 'blubber ha crib',
  donationCost: 25694,
  roomId: 'irritably uniform spiteful',
  status: 'likewise hearsay',
};

export const sampleWithNewData: NewDonationTable = {
  donationId: 'about incidentally',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
