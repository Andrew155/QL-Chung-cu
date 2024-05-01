import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RequestTableComponent } from './list/request-table.component';
import { RequestTableDetailComponent } from './detail/request-table-detail.component';
import { RequestTableUpdateComponent } from './update/request-table-update.component';
import RequestTableResolve from './route/request-table-routing-resolve.service';

const requestTableRoute: Routes = [
  {
    path: '',
    component: RequestTableComponent,
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RequestTableDetailComponent,
    resolve: {
      requestTable: RequestTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RequestTableUpdateComponent,
    resolve: {
      requestTable: RequestTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RequestTableUpdateComponent,
    resolve: {
      requestTable: RequestTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default requestTableRoute;
