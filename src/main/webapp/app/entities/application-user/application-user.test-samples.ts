import dayjs from 'dayjs/esm';

import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 3781,
  citizenID: 'questioningly sneaker',
};

export const sampleWithPartialData: IApplicationUser = {
  id: 2157,
  updateAt: dayjs('2024-04-20T16:44'),
  familyId: 'onto',
  citizenID: 'aw',
  status: 'sadly as and',
  roomId: 'from chicken',
};

export const sampleWithFullData: IApplicationUser = {
  id: 24591,
  createAt: dayjs('2024-04-20T14:46'),
  updateAt: dayjs('2024-04-20T22:04'),
  deletedAt: dayjs('2024-04-20T13:16'),
  familyId: 'foster ick where',
  citizenID: 'assume',
  name: 'fax sun',
  dob: 'measure bah',
  contact: 'phosphate pace',
  gender: 'absent furthermore',
  relation: 'seriously',
  status: 'off sheepishly sustain',
  roomId: 'past until',
};

export const sampleWithNewData: NewApplicationUser = {
  citizenID: 'tassel flow',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
