import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'd8108b05-6bbf-476b-bd34-2cce87878e9e',
};

export const sampleWithPartialData: IAuthority = {
  name: 'fc066986-4f54-4d89-a0d0-330195b3e609',
};

export const sampleWithFullData: IAuthority = {
  name: '48411fbf-f917-4d75-853b-b7cfc7420b42',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
