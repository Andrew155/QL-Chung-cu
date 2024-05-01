import dayjs from 'dayjs/esm';

import { IVehicleTable, NewVehicleTable } from './vehicle-table.model';

export const sampleWithRequiredData: IVehicleTable = {
  id: 27536,
  vehicleId: 'apropos',
};

export const sampleWithPartialData: IVehicleTable = {
  id: 20520,
  createAt: dayjs('2024-04-21T01:12'),
  deletedAt: dayjs('2024-04-20T09:18'),
  vehicleId: 'angrily',
  ownerId: 'provided tensely',
};

export const sampleWithFullData: IVehicleTable = {
  id: 21300,
  createAt: dayjs('2024-04-20T10:34'),
  updateAt: dayjs('2024-04-20T13:43'),
  deletedAt: dayjs('2024-04-20T05:05'),
  vehicleName: 'elixir',
  vehicleType: 'urgently up cool',
  vehicleId: 'woot',
  roomId: 'um usable sans',
  ownerId: 'offend cure brightly',
  vehicleFee: 7774,
};

export const sampleWithNewData: NewVehicleTable = {
  vehicleId: 'hmph manipulation sophisticated',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
