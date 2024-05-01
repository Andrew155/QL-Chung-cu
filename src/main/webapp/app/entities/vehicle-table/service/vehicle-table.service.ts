import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleTable, NewVehicleTable } from '../vehicle-table.model';

export type PartialUpdateVehicleTable = Partial<IVehicleTable> & Pick<IVehicleTable, 'id'>;

type RestOf<T extends IVehicleTable | NewVehicleTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestVehicleTable = RestOf<IVehicleTable>;

export type NewRestVehicleTable = RestOf<NewVehicleTable>;

export type PartialUpdateRestVehicleTable = RestOf<PartialUpdateVehicleTable>;

export type EntityResponseType = HttpResponse<IVehicleTable>;
export type EntityArrayResponseType = HttpResponse<IVehicleTable[]>;

@Injectable({ providedIn: 'root' })
export class VehicleTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-tables');

  create(vehicleTable: NewVehicleTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleTable);
    return this.http
      .post<RestVehicleTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(vehicleTable: IVehicleTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleTable);
    return this.http
      .put<RestVehicleTable>(`${this.resourceUrl}/${this.getVehicleTableIdentifier(vehicleTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(vehicleTable: PartialUpdateVehicleTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleTable);
    return this.http
      .patch<RestVehicleTable>(`${this.resourceUrl}/${this.getVehicleTableIdentifier(vehicleTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVehicleTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVehicleTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVehicleTableIdentifier(vehicleTable: Pick<IVehicleTable, 'id'>): number {
    return vehicleTable.id;
  }

  compareVehicleTable(o1: Pick<IVehicleTable, 'id'> | null, o2: Pick<IVehicleTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getVehicleTableIdentifier(o1) === this.getVehicleTableIdentifier(o2) : o1 === o2;
  }

  addVehicleTableToCollectionIfMissing<Type extends Pick<IVehicleTable, 'id'>>(
    vehicleTableCollection: Type[],
    ...vehicleTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const vehicleTables: Type[] = vehicleTablesToCheck.filter(isPresent);
    if (vehicleTables.length > 0) {
      const vehicleTableCollectionIdentifiers = vehicleTableCollection.map(vehicleTableItem =>
        this.getVehicleTableIdentifier(vehicleTableItem),
      );
      const vehicleTablesToAdd = vehicleTables.filter(vehicleTableItem => {
        const vehicleTableIdentifier = this.getVehicleTableIdentifier(vehicleTableItem);
        if (vehicleTableCollectionIdentifiers.includes(vehicleTableIdentifier)) {
          return false;
        }
        vehicleTableCollectionIdentifiers.push(vehicleTableIdentifier);
        return true;
      });
      return [...vehicleTablesToAdd, ...vehicleTableCollection];
    }
    return vehicleTableCollection;
  }

  protected convertDateFromClient<T extends IVehicleTable | NewVehicleTable | PartialUpdateVehicleTable>(vehicleTable: T): RestOf<T> {
    return {
      ...vehicleTable,
      createAt: vehicleTable.createAt?.toJSON() ?? null,
      updateAt: vehicleTable.updateAt?.toJSON() ?? null,
      deletedAt: vehicleTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVehicleTable: RestVehicleTable): IVehicleTable {
    return {
      ...restVehicleTable,
      createAt: restVehicleTable.createAt ? dayjs(restVehicleTable.createAt) : undefined,
      updateAt: restVehicleTable.updateAt ? dayjs(restVehicleTable.updateAt) : undefined,
      deletedAt: restVehicleTable.deletedAt ? dayjs(restVehicleTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVehicleTable>): HttpResponse<IVehicleTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVehicleTable[]>): HttpResponse<IVehicleTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
