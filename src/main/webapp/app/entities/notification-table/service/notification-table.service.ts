import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotificationTable, NewNotificationTable } from '../notification-table.model';

export type PartialUpdateNotificationTable = Partial<INotificationTable> & Pick<INotificationTable, 'id'>;

type RestOf<T extends INotificationTable | NewNotificationTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestNotificationTable = RestOf<INotificationTable>;

export type NewRestNotificationTable = RestOf<NewNotificationTable>;

export type PartialUpdateRestNotificationTable = RestOf<PartialUpdateNotificationTable>;

export type EntityResponseType = HttpResponse<INotificationTable>;
export type EntityArrayResponseType = HttpResponse<INotificationTable[]>;

@Injectable({ providedIn: 'root' })
export class NotificationTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notification-tables');

  create(notificationTable: NewNotificationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTable);
    return this.http
      .post<RestNotificationTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(notificationTable: INotificationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTable);
    return this.http
      .put<RestNotificationTable>(`${this.resourceUrl}/${this.getNotificationTableIdentifier(notificationTable)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(notificationTable: PartialUpdateNotificationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificationTable);
    return this.http
      .patch<RestNotificationTable>(`${this.resourceUrl}/${this.getNotificationTableIdentifier(notificationTable)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestNotificationTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestNotificationTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getNotificationTableIdentifier(notificationTable: Pick<INotificationTable, 'id'>): number {
    return notificationTable.id;
  }

  compareNotificationTable(o1: Pick<INotificationTable, 'id'> | null, o2: Pick<INotificationTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getNotificationTableIdentifier(o1) === this.getNotificationTableIdentifier(o2) : o1 === o2;
  }

  addNotificationTableToCollectionIfMissing<Type extends Pick<INotificationTable, 'id'>>(
    notificationTableCollection: Type[],
    ...notificationTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const notificationTables: Type[] = notificationTablesToCheck.filter(isPresent);
    if (notificationTables.length > 0) {
      const notificationTableCollectionIdentifiers = notificationTableCollection.map(notificationTableItem =>
        this.getNotificationTableIdentifier(notificationTableItem),
      );
      const notificationTablesToAdd = notificationTables.filter(notificationTableItem => {
        const notificationTableIdentifier = this.getNotificationTableIdentifier(notificationTableItem);
        if (notificationTableCollectionIdentifiers.includes(notificationTableIdentifier)) {
          return false;
        }
        notificationTableCollectionIdentifiers.push(notificationTableIdentifier);
        return true;
      });
      return [...notificationTablesToAdd, ...notificationTableCollection];
    }
    return notificationTableCollection;
  }

  protected convertDateFromClient<T extends INotificationTable | NewNotificationTable | PartialUpdateNotificationTable>(
    notificationTable: T,
  ): RestOf<T> {
    return {
      ...notificationTable,
      createAt: notificationTable.createAt?.toJSON() ?? null,
      updateAt: notificationTable.updateAt?.toJSON() ?? null,
      deletedAt: notificationTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restNotificationTable: RestNotificationTable): INotificationTable {
    return {
      ...restNotificationTable,
      createAt: restNotificationTable.createAt ? dayjs(restNotificationTable.createAt) : undefined,
      updateAt: restNotificationTable.updateAt ? dayjs(restNotificationTable.updateAt) : undefined,
      deletedAt: restNotificationTable.deletedAt ? dayjs(restNotificationTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestNotificationTable>): HttpResponse<INotificationTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestNotificationTable[]>): HttpResponse<INotificationTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
