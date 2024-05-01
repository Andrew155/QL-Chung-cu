import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonationTable } from '../donation-table.model';
import { DonationTableService } from '../service/donation-table.service';

const donationTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IDonationTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(DonationTableService)
      .find(id)
      .pipe(
        mergeMap((donationTable: HttpResponse<IDonationTable>) => {
          if (donationTable.body) {
            return of(donationTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default donationTableResolve;
