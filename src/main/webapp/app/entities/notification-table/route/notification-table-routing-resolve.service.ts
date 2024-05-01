import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationTable } from '../notification-table.model';
import { NotificationTableService } from '../service/notification-table.service';

const notificationTableResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationTable> => {
  const id = route.params['id'];
  if (id) {
    return inject(NotificationTableService)
      .find(id)
      .pipe(
        mergeMap((notificationTable: HttpResponse<INotificationTable>) => {
          if (notificationTable.body) {
            return of(notificationTable.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default notificationTableResolve;
