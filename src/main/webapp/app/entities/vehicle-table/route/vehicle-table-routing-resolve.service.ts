import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleTable } from '../vehicle-table.model';
import { VehicleTableService } from '../service/vehicle-table.service';

const vehicleTableResolve = (route: ActivatedRouteSnapshot): Observable<null | IVehicleTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(VehicleTableService)
      .find(id)
      .pipe(
        mergeMap((vehicleTable: HttpResponse<IVehicleTable>) => {
          if (vehicleTable.body) {
            return of(vehicleTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default vehicleTableResolve;
