import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CitizenTableComponent } from './list/citizen-table.component';
import { CitizenTableDetailComponent } from './detail/citizen-table-detail.component';
import { CitizenTableUpdateComponent } from './update/citizen-table-update.component';
import CitizenTableResolve from './route/citizen-table-routing-resolve.service';

const citizenTableRoute: Routes = [
  {
    path: '',
    component: CitizenTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CitizenTableDetailComponent,
    resolve: {
      citizenTable: CitizenTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CitizenTableUpdateComponent,
    resolve: {
      citizenTable: CitizenTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CitizenTableUpdateComponent,
    resolve: {
      citizenTable: CitizenTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default citizenTableRoute;
