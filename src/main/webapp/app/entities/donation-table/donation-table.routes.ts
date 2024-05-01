import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DonationTableComponent } from './list/donation-table.component';
import { DonationTableDetailComponent } from './detail/donation-table-detail.component';
import { DonationTableUpdateComponent } from './update/donation-table-update.component';
import DonationTableResolve from './route/donation-table-routing-resolve.service';

const donationTableRoute: Routes = [
  {
    path: '',
    component: DonationTableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonationTableDetailComponent,
    resolve: {
      donationTable: DonationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonationTableUpdateComponent,
    resolve: {
      donationTable: DonationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonationTableUpdateComponent,
    resolve: {
      donationTable: DonationTableResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default donationTableRoute;
