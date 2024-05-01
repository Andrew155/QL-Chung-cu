import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoomTableComponent } from './list/room-table.component';
import { RoomTableDetailComponent } from './detail/room-table-detail.component';
import { RoomTableUpdateComponent } from './update/room-table-update.component';
import RoomTableResolve from './route/room-table-routing-resolve.service';

const roomTableRoute: Routes = [
  {
    path: '',
    component: RoomTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoomTableDetailComponent,
    resolve: {
      roomTable: RoomTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoomTableUpdateComponent,
    resolve: {
      roomTable: RoomTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoomTableUpdateComponent,
    resolve: {
      roomTable: RoomTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roomTableRoute;
