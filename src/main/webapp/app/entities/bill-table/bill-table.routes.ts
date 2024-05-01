import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BillTableComponent } from './list/bill-table.component';
import { BillTableDetailComponent } from './detail/bill-table-detail.component';
import { BillTableUpdateComponent } from './update/bill-table-update.component';
import BillTableResolve from './route/bill-table-routing-resolve.service';

const billTableRoute: Routes = [
  {
    path: '',
    component: BillTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BillTableDetailComponent,
    resolve: {
      billTable: BillTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BillTableUpdateComponent,
    resolve: {
      billTable: BillTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BillTableUpdateComponent,
    resolve: {
      billTable: BillTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default billTableRoute;
