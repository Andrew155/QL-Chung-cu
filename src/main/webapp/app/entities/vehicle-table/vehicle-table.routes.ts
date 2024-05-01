import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { VehicleTableComponent } from './list/vehicle-table.component';
import { VehicleTableDetailComponent } from './detail/vehicle-table-detail.component';
import { VehicleTableUpdateComponent } from './update/vehicle-table-update.component';
import VehicleTableResolve from './route/vehicle-table-routing-resolve.service';

const vehicleTableRoute: Routes = [
  {
    path: '',
    component: VehicleTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehicleTableDetailComponent,
    resolve: {
      vehicleTable: VehicleTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehicleTableUpdateComponent,
    resolve: {
      vehicleTable: VehicleTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehicleTableUpdateComponent,
    resolve: {
      vehicleTable: VehicleTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default vehicleTableRoute;
