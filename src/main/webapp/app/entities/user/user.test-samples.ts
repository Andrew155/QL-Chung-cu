import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 8717,
  login: '&bB@Hhk\\MyqlS\\VsTc3\\Mg8EJp',
};

export const sampleWithPartialData: IUser = {
  id: 25139,
  login: 'h5@',
};

export const sampleWithFullData: IUser = {
  id: 28148,
  login: 'Ng*X@HxG-b',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
