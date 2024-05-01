import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRequestTable } from '../request-table.model';
import { RequestTableService } from '../service/request-table.service';

const requestTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IRequestTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(RequestTableService)
      .find(id)
      .pipe(
        mergeMap((requestTable: HttpResponse<IRequestTable>) => {
          if (requestTable.body) {
            return of(requestTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default requestTableResolve;
