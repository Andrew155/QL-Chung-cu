import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FeeTableComponent } from './list/fee-table.component';
import { FeeTableDetailComponent } from './detail/fee-table-detail.component';
import { FeeTableUpdateComponent } from './update/fee-table-update.component';
import FeeTableResolve from './route/fee-table-routing-resolve.service';

const feeTableRoute: Routes = [
  {
    path: '',
    component: FeeTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FeeTableDetailComponent,
    resolve: {
      feeTable: FeeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FeeTableUpdateComponent,
    resolve: {
      feeTable: FeeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FeeTableUpdateComponent,
    resolve: {
      feeTable: FeeTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default feeTableRoute;
