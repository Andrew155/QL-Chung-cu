import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFeeTable, NewFeeTable } from '../fee-table.model';

export type PartialUpdateFeeTable = Partial<IFeeTable> & Pick<IFeeTable, 'id'>;

type RestOf<T extends IFeeTable | NewFeeTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt' | 'date'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
  date?: string | null;
};

export type RestFeeTable = RestOf<IFeeTable>;

export type NewRestFeeTable = RestOf<NewFeeTable>;

export type PartialUpdateRestFeeTable = RestOf<PartialUpdateFeeTable>;

export type EntityResponseType = HttpResponse<IFeeTable>;
export type EntityArrayResponseType = HttpResponse<IFeeTable[]>;

@Injectable({ providedIn: 'root' })
export class FeeTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fee-tables');

  create(feeTable: NewFeeTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feeTable);
    return this.http
      .post<RestFeeTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(feeTable: IFeeTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feeTable);
    return this.http
      .put<RestFeeTable>(`${this.resourceUrl}/${this.getFeeTableIdentifier(feeTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(feeTable: PartialUpdateFeeTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(feeTable);
    return this.http
      .patch<RestFeeTable>(`${this.resourceUrl}/${this.getFeeTableIdentifier(feeTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFeeTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFeeTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFeeTableIdentifier(feeTable: Pick<IFeeTable, 'id'>): number {
    return feeTable.id;
  }

  compareFeeTable(o1: Pick<IFeeTable, 'id'> | null, o2: Pick<IFeeTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getFeeTableIdentifier(o1) === this.getFeeTableIdentifier(o2) : o1 === o2;
  }

  addFeeTableToCollectionIfMissing<Type extends Pick<IFeeTable, 'id'>>(
    feeTableCollection: Type[],
    ...feeTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const feeTables: Type[] = feeTablesToCheck.filter(isPresent);
    if (feeTables.length > 0) {
      const feeTableCollectionIdentifiers = feeTableCollection.map(feeTableItem => this.getFeeTableIdentifier(feeTableItem));
      const feeTablesToAdd = feeTables.filter(feeTableItem => {
        const feeTableIdentifier = this.getFeeTableIdentifier(feeTableItem);
        if (feeTableCollectionIdentifiers.includes(feeTableIdentifier)) {
          return false;
        }
        feeTableCollectionIdentifiers.push(feeTableIdentifier);
        return true;
      });
      return [...feeTablesToAdd, ...feeTableCollection];
    }
    return feeTableCollection;
  }

  protected convertDateFromClient<T extends IFeeTable | NewFeeTable | PartialUpdateFeeTable>(feeTable: T): RestOf<T> {
    return {
      ...feeTable,
      createAt: feeTable.createAt?.toJSON() ?? null,
      updateAt: feeTable.updateAt?.toJSON() ?? null,
      deletedAt: feeTable.deletedAt?.toJSON() ?? null,
      date: feeTable.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFeeTable: RestFeeTable): IFeeTable {
    return {
      ...restFeeTable,
      createAt: restFeeTable.createAt ? dayjs(restFeeTable.createAt) : undefined,
      updateAt: restFeeTable.updateAt ? dayjs(restFeeTable.updateAt) : undefined,
      deletedAt: restFeeTable.deletedAt ? dayjs(restFeeTable.deletedAt) : undefined,
      date: restFeeTable.date ? dayjs(restFeeTable.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFeeTable>): HttpResponse<IFeeTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFeeTable[]>): HttpResponse<IFeeTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
