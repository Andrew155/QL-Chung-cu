import dayjs from 'dayjs/esm';

import { ICitizenTable, NewCitizenTable } from './citizen-table.model';

export const sampleWithRequiredData: ICitizenTable = {
  id: 14641,
  citizenID: 'shell indeed besides',
};

export const sampleWithPartialData: ICitizenTable = {
  id: 29407,
  citizenID: 'infamous whoever huzzah',
  name: 'wiggle',
  dob: 'tube kissingly plover',
  contact: 'usually check ah',
  gender: 'yuck shocked',
  relation: 'inasmuch ew',
};

export const sampleWithFullData: ICitizenTable = {
  id: 8908,
  createAt: dayjs('2024-04-20T12:48'),
  updateAt: dayjs('2024-04-20T18:17'),
  deletedAt: dayjs('2024-04-20T11:02'),
  citizenID: 'eek oh',
  name: 'minus fixed than',
  dob: 'turbulent',
  contact: 'petrify consequence repeatedly',
  gender: 'until',
  relation: 'shakily',
  status: 'mistrust',
};

export const sampleWithNewData: NewCitizenTable = {
  citizenID: 'heed conscious',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
