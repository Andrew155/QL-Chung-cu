import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFeeTable } from '../fee-table.model';
import { FeeTableService } from '../service/fee-table.service';

const feeTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IFeeTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(FeeTableService)
      .find(id)
      .pipe(
        mergeMap((feeTable: HttpResponse<IFeeTable>) => {
          if (feeTable.body) {
            return of(feeTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default feeTableResolve;
