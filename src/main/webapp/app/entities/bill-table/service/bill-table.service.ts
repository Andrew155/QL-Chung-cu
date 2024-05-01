import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBillTable, NewBillTable } from '../bill-table.model';

export type PartialUpdateBillTable = Partial<IBillTable> & Pick<IBillTable, 'id'>;

type RestOf<T extends IBillTable | NewBillTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt' | 'date'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
  date?: string | null;
};

export type RestBillTable = RestOf<IBillTable>;

export type NewRestBillTable = RestOf<NewBillTable>;

export type PartialUpdateRestBillTable = RestOf<PartialUpdateBillTable>;

export type EntityResponseType = HttpResponse<IBillTable>;
export type EntityArrayResponseType = HttpResponse<IBillTable[]>;

@Injectable({ providedIn: 'root' })
export class BillTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bill-tables');

  create(billTable: NewBillTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billTable);
    return this.http
      .post<RestBillTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(billTable: IBillTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billTable);
    return this.http
      .put<RestBillTable>(`${this.resourceUrl}/${this.getBillTableIdentifier(billTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(billTable: PartialUpdateBillTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billTable);
    return this.http
      .patch<RestBillTable>(`${this.resourceUrl}/${this.getBillTableIdentifier(billTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBillTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBillTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBillTableIdentifier(billTable: Pick<IBillTable, 'id'>): number {
    return billTable.id;
  }

  compareBillTable(o1: Pick<IBillTable, 'id'> | null, o2: Pick<IBillTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getBillTableIdentifier(o1) === this.getBillTableIdentifier(o2) : o1 === o2;
  }

  addBillTableToCollectionIfMissing<Type extends Pick<IBillTable, 'id'>>(
    billTableCollection: Type[],
    ...billTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const billTables: Type[] = billTablesToCheck.filter(isPresent);
    if (billTables.length > 0) {
      const billTableCollectionIdentifiers = billTableCollection.map(billTableItem => this.getBillTableIdentifier(billTableItem));
      const billTablesToAdd = billTables.filter(billTableItem => {
        const billTableIdentifier = this.getBillTableIdentifier(billTableItem);
        if (billTableCollectionIdentifiers.includes(billTableIdentifier)) {
          return false;
        }
        billTableCollectionIdentifiers.push(billTableIdentifier);
        return true;
      });
      return [...billTablesToAdd, ...billTableCollection];
    }
    return billTableCollection;
  }

  protected convertDateFromClient<T extends IBillTable | NewBillTable | PartialUpdateBillTable>(billTable: T): RestOf<T> {
    return {
      ...billTable,
      createAt: billTable.createAt?.toJSON() ?? null,
      updateAt: billTable.updateAt?.toJSON() ?? null,
      deletedAt: billTable.deletedAt?.toJSON() ?? null,
      date: billTable.date?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBillTable: RestBillTable): IBillTable {
    return {
      ...restBillTable,
      createAt: restBillTable.createAt ? dayjs(restBillTable.createAt) : undefined,
      updateAt: restBillTable.updateAt ? dayjs(restBillTable.updateAt) : undefined,
      deletedAt: restBillTable.deletedAt ? dayjs(restBillTable.deletedAt) : undefined,
      date: restBillTable.date ? dayjs(restBillTable.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBillTable>): HttpResponse<IBillTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBillTable[]>): HttpResponse<IBillTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
