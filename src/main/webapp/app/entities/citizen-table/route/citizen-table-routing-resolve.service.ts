import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICitizenTable } from '../citizen-table.model';
import { CitizenTableService } from '../service/citizen-table.service';

const citizenTableResolve = (route: ActivatedRouteSnapshot): Observable<null | ICitizenTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(CitizenTableService)
      .find(id)
      .pipe(
        mergeMap((citizenTable: HttpResponse<ICitizenTable>) => {
          if (citizenTable.body) {
            return of(citizenTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default citizenTableResolve;
