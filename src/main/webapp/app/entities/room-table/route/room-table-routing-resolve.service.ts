import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRoomTable } from '../room-table.model';
import { RoomTableService } from '../service/room-table.service';

const roomTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IRoomTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(RoomTableService)
      .find(id)
      .pipe(
        mergeMap((roomTable: HttpResponse<IRoomTable>) => {
          if (roomTable.body) {
            return of(roomTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default roomTableResolve;
