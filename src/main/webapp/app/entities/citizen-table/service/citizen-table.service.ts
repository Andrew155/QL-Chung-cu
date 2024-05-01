import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICitizenTable, NewCitizenTable } from '../citizen-table.model';

export type PartialUpdateCitizenTable = Partial<ICitizenTable> & Pick<ICitizenTable, 'id'>;

type RestOf<T extends ICitizenTable | NewCitizenTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestCitizenTable = RestOf<ICitizenTable>;

export type NewRestCitizenTable = RestOf<NewCitizenTable>;

export type PartialUpdateRestCitizenTable = RestOf<PartialUpdateCitizenTable>;

export type EntityResponseType = HttpResponse<ICitizenTable>;
export type EntityArrayResponseType = HttpResponse<ICitizenTable[]>;

@Injectable({ providedIn: 'root' })
export class CitizenTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/citizen-tables');

  create(citizenTable: NewCitizenTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citizenTable);
    return this.http
      .post<RestCitizenTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(citizenTable: ICitizenTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citizenTable);
    return this.http
      .put<RestCitizenTable>(`${this.resourceUrl}/${this.getCitizenTableIdentifier(citizenTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(citizenTable: PartialUpdateCitizenTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(citizenTable);
    return this.http
      .patch<RestCitizenTable>(`${this.resourceUrl}/${this.getCitizenTableIdentifier(citizenTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCitizenTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCitizenTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCitizenTableIdentifier(citizenTable: Pick<ICitizenTable, 'id'>): number {
    return citizenTable.id;
  }

  compareCitizenTable(o1: Pick<ICitizenTable, 'id'> | null, o2: Pick<ICitizenTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getCitizenTableIdentifier(o1) === this.getCitizenTableIdentifier(o2) : o1 === o2;
  }

  addCitizenTableToCollectionIfMissing<Type extends Pick<ICitizenTable, 'id'>>(
    citizenTableCollection: Type[],
    ...citizenTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const citizenTables: Type[] = citizenTablesToCheck.filter(isPresent);
    if (citizenTables.length > 0) {
      const citizenTableCollectionIdentifiers = citizenTableCollection.map(citizenTableItem =>
        this.getCitizenTableIdentifier(citizenTableItem),
      );
      const citizenTablesToAdd = citizenTables.filter(citizenTableItem => {
        const citizenTableIdentifier = this.getCitizenTableIdentifier(citizenTableItem);
        if (citizenTableCollectionIdentifiers.includes(citizenTableIdentifier)) {
          return false;
        }
        citizenTableCollectionIdentifiers.push(citizenTableIdentifier);
        return true;
      });
      return [...citizenTablesToAdd, ...citizenTableCollection];
    }
    return citizenTableCollection;
  }

  protected convertDateFromClient<T extends ICitizenTable | NewCitizenTable | PartialUpdateCitizenTable>(citizenTable: T): RestOf<T> {
    return {
      ...citizenTable,
      createAt: citizenTable.createAt?.toJSON() ?? null,
      updateAt: citizenTable.updateAt?.toJSON() ?? null,
      deletedAt: citizenTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCitizenTable: RestCitizenTable): ICitizenTable {
    return {
      ...restCitizenTable,
      createAt: restCitizenTable.createAt ? dayjs(restCitizenTable.createAt) : undefined,
      updateAt: restCitizenTable.updateAt ? dayjs(restCitizenTable.updateAt) : undefined,
      deletedAt: restCitizenTable.deletedAt ? dayjs(restCitizenTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCitizenTable>): HttpResponse<ICitizenTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCitizenTable[]>): HttpResponse<ICitizenTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
