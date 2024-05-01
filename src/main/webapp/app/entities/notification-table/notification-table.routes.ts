import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificationTableComponent } from './list/notification-table.component';
import { NotificationTableDetailComponent } from './detail/notification-table-detail.component';
import { NotificationTableUpdateComponent } from './update/notification-table-update.component';
import NotificationTableResolve from './route/notification-table-routing-resolve.service';

const notificationTableRoute: Routes = [
  {
    path: '',
    component: NotificationTableComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificationTableDetailComponent,
    resolve: {
      notificationTable: NotificationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificationTableUpdateComponent,
    resolve: {
      notificationTable: NotificationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificationTableUpdateComponent,
    resolve: {
      notificationTable: NotificationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificationTableRoute;
