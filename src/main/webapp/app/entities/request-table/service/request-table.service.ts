import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRequestTable, NewRequestTable } from '../request-table.model';

export type PartialUpdateRequestTable = Partial<IRequestTable> & Pick<IRequestTable, 'id'>;

type RestOf<T extends IRequestTable | NewRequestTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestRequestTable = RestOf<IRequestTable>;

export type NewRestRequestTable = RestOf<NewRequestTable>;

export type PartialUpdateRestRequestTable = RestOf<PartialUpdateRequestTable>;

export type EntityResponseType = HttpResponse<IRequestTable>;
export type EntityArrayResponseType = HttpResponse<IRequestTable[]>;

@Injectable({ providedIn: 'root' })
export class RequestTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/request-tables');

  create(requestTable: NewRequestTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requestTable);
    return this.http
      .post<RestRequestTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(requestTable: IRequestTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requestTable);
    return this.http
      .put<RestRequestTable>(`${this.resourceUrl}/${this.getRequestTableIdentifier(requestTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(requestTable: PartialUpdateRequestTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(requestTable);
    return this.http
      .patch<RestRequestTable>(`${this.resourceUrl}/${this.getRequestTableIdentifier(requestTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRequestTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRequestTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRequestTableIdentifier(requestTable: Pick<IRequestTable, 'id'>): number {
    return requestTable.id;
  }

  compareRequestTable(o1: Pick<IRequestTable, 'id'> | null, o2: Pick<IRequestTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getRequestTableIdentifier(o1) === this.getRequestTableIdentifier(o2) : o1 === o2;
  }

  addRequestTableToCollectionIfMissing<Type extends Pick<IRequestTable, 'id'>>(
    requestTableCollection: Type[],
    ...requestTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const requestTables: Type[] = requestTablesToCheck.filter(isPresent);
    if (requestTables.length > 0) {
      const requestTableCollectionIdentifiers = requestTableCollection.map(requestTableItem =>
        this.getRequestTableIdentifier(requestTableItem),
      );
      const requestTablesToAdd = requestTables.filter(requestTableItem => {
        const requestTableIdentifier = this.getRequestTableIdentifier(requestTableItem);
        if (requestTableCollectionIdentifiers.includes(requestTableIdentifier)) {
          return false;
        }
        requestTableCollectionIdentifiers.push(requestTableIdentifier);
        return true;
      });
      return [...requestTablesToAdd, ...requestTableCollection];
    }
    return requestTableCollection;
  }

  protected convertDateFromClient<T extends IRequestTable | NewRequestTable | PartialUpdateRequestTable>(requestTable: T): RestOf<T> {
    return {
      ...requestTable,
      createAt: requestTable.createAt?.toJSON() ?? null,
      updateAt: requestTable.updateAt?.toJSON() ?? null,
      deletedAt: requestTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRequestTable: RestRequestTable): IRequestTable {
    return {
      ...restRequestTable,
      createAt: restRequestTable.createAt ? dayjs(restRequestTable.createAt) : undefined,
      updateAt: restRequestTable.updateAt ? dayjs(restRequestTable.updateAt) : undefined,
      deletedAt: restRequestTable.deletedAt ? dayjs(restRequestTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRequestTable>): HttpResponse<IRequestTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRequestTable[]>): HttpResponse<IRequestTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
