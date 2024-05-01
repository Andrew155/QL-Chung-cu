import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBillTable } from '../bill-table.model';
import { BillTableService } from '../service/bill-table.service';

const billTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IBillTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(BillTableService)
      .find(id)
      .pipe(
        mergeMap((billTable: HttpResponse<IBillTable>) => {
          if (billTable.body) {
            return of(billTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default billTableResolve;
