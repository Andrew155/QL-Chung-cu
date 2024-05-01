import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDonationTable, NewDonationTable } from '../donation-table.model';

export type PartialUpdateDonationTable = Partial<IDonationTable> & Pick<IDonationTable, 'id'>;

type RestOf<T extends IDonationTable | NewDonationTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestDonationTable = RestOf<IDonationTable>;

export type NewRestDonationTable = RestOf<NewDonationTable>;

export type PartialUpdateRestDonationTable = RestOf<PartialUpdateDonationTable>;

export type EntityResponseType = HttpResponse<IDonationTable>;
export type EntityArrayResponseType = HttpResponse<IDonationTable[]>;

@Injectable({ providedIn: 'root' })
export class DonationTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/donation-tables');

  create(donationTable: NewDonationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTable);
    return this.http
      .post<RestDonationTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(donationTable: IDonationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTable);
    return this.http
      .put<RestDonationTable>(`${this.resourceUrl}/${this.getDonationTableIdentifier(donationTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(donationTable: PartialUpdateDonationTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(donationTable);
    return this.http
      .patch<RestDonationTable>(`${this.resourceUrl}/${this.getDonationTableIdentifier(donationTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDonationTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDonationTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDonationTableIdentifier(donationTable: Pick<IDonationTable, 'id'>): number {
    return donationTable.id;
  }

  compareDonationTable(o1: Pick<IDonationTable, 'id'> | null, o2: Pick<IDonationTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getDonationTableIdentifier(o1) === this.getDonationTableIdentifier(o2) : o1 === o2;
  }

  addDonationTableToCollectionIfMissing<Type extends Pick<IDonationTable, 'id'>>(
    donationTableCollection: Type[],
    ...donationTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const donationTables: Type[] = donationTablesToCheck.filter(isPresent);
    if (donationTables.length > 0) {
      const donationTableCollectionIdentifiers = donationTableCollection.map(donationTableItem =>
        this.getDonationTableIdentifier(donationTableItem),
      );
      const donationTablesToAdd = donationTables.filter(donationTableItem => {
        const donationTableIdentifier = this.getDonationTableIdentifier(donationTableItem);
        if (donationTableCollectionIdentifiers.includes(donationTableIdentifier)) {
          return false;
        }
        donationTableCollectionIdentifiers.push(donationTableIdentifier);
        return true;
      });
      return [...donationTablesToAdd, ...donationTableCollection];
    }
    return donationTableCollection;
  }

  protected convertDateFromClient<T extends IDonationTable | NewDonationTable | PartialUpdateDonationTable>(donationTable: T): RestOf<T> {
    return {
      ...donationTable,
      createAt: donationTable.createAt?.toJSON() ?? null,
      updateAt: donationTable.updateAt?.toJSON() ?? null,
      deletedAt: donationTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDonationTable: RestDonationTable): IDonationTable {
    return {
      ...restDonationTable,
      createAt: restDonationTable.createAt ? dayjs(restDonationTable.createAt) : undefined,
      updateAt: restDonationTable.updateAt ? dayjs(restDonationTable.updateAt) : undefined,
      deletedAt: restDonationTable.deletedAt ? dayjs(restDonationTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDonationTable>): HttpResponse<IDonationTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDonationTable[]>): HttpResponse<IDonationTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
