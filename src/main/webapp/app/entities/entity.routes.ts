import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'qLchungcuApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'application-user',
    data: { pageTitle: 'qLchungcuApp.applicationUser.home.title' },
    loadChildren: () => import('./application-user/application-user.routes'),
  },
  {
    path: 'citizen-table',
    data: { pageTitle: 'qLchungcuApp.citizenTable.home.title' },
    loadChildren: () => import('./citizen-table/citizen-table.routes'),
  },
  {
    path: 'room-table',
    data: { pageTitle: 'qLchungcuApp.roomTable.home.title' },
    loadChildren: () => import('./room-table/room-table.routes'),
  },
  {
    path: 'fee-table',
    data: { pageTitle: 'qLchungcuApp.feeTable.home.title' },
    loadChildren: () => import('./fee-table/fee-table.routes'),
  },
  {
    path: 'bill-table',
    data: { pageTitle: 'qLchungcuApp.billTable.home.title' },
    loadChildren: () => import('./bill-table/bill-table.routes'),
  },
  {
    path: 'request-table',
    data: { pageTitle: 'qLchungcuApp.requestTable.home.title' },
    loadChildren: () => import('./request-table/request-table.routes'),
  },
  {
    path: 'notification-table',
    data: { pageTitle: 'qLchungcuApp.notificationTable.home.title' },
    loadChildren: () => import('./notification-table/notification-table.routes'),
  },
  {
    path: 'vehicle-table',
    data: { pageTitle: 'qLchungcuApp.vehicleTable.home.title' },
    loadChildren: () => import('./vehicle-table/vehicle-table.routes'),
  },
  {
    path: 'donation-table',
    data: { pageTitle: 'qLchungcuApp.donationTable.home.title' },
    loadChildren: () => import('./donation-table/donation-table.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
