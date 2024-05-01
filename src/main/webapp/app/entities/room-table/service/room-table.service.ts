import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRoomTable, NewRoomTable } from '../room-table.model';

export type PartialUpdateRoomTable = Partial<IRoomTable> & Pick<IRoomTable, 'id'>;

type RestOf<T extends IRoomTable | NewRoomTable> = Omit<T, 'createAt' | 'updateAt' | 'deletedAt'> & {
  createAt?: string | null;
  updateAt?: string | null;
  deletedAt?: string | null;
};

export type RestRoomTable = RestOf<IRoomTable>;

export type NewRestRoomTable = RestOf<NewRoomTable>;

export type PartialUpdateRestRoomTable = RestOf<PartialUpdateRoomTable>;

export type EntityResponseType = HttpResponse<IRoomTable>;
export type EntityArrayResponseType = HttpResponse<IRoomTable[]>;

@Injectable({ providedIn: 'root' })
export class RoomTableService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/room-tables');

  create(roomTable: NewRoomTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomTable);
    return this.http
      .post<RestRoomTable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(roomTable: IRoomTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomTable);
    return this.http
      .put<RestRoomTable>(`${this.resourceUrl}/${this.getRoomTableIdentifier(roomTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(roomTable: PartialUpdateRoomTable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(roomTable);
    return this.http
      .patch<RestRoomTable>(`${this.resourceUrl}/${this.getRoomTableIdentifier(roomTable)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestRoomTable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestRoomTable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getRoomTableIdentifier(roomTable: Pick<IRoomTable, 'id'>): number {
    return roomTable.id;
  }

  compareRoomTable(o1: Pick<IRoomTable, 'id'> | null, o2: Pick<IRoomTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getRoomTableIdentifier(o1) === this.getRoomTableIdentifier(o2) : o1 === o2;
  }

  addRoomTableToCollectionIfMissing<Type extends Pick<IRoomTable, 'id'>>(
    roomTableCollection: Type[],
    ...roomTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const roomTables: Type[] = roomTablesToCheck.filter(isPresent);
    if (roomTables.length > 0) {
      const roomTableCollectionIdentifiers = roomTableCollection.map(roomTableItem => this.getRoomTableIdentifier(roomTableItem));
      const roomTablesToAdd = roomTables.filter(roomTableItem => {
        const roomTableIdentifier = this.getRoomTableIdentifier(roomTableItem);
        if (roomTableCollectionIdentifiers.includes(roomTableIdentifier)) {
          return false;
        }
        roomTableCollectionIdentifiers.push(roomTableIdentifier);
        return true;
      });
      return [...roomTablesToAdd, ...roomTableCollection];
    }
    return roomTableCollection;
  }

  protected convertDateFromClient<T extends IRoomTable | NewRoomTable | PartialUpdateRoomTable>(roomTable: T): RestOf<T> {
    return {
      ...roomTable,
      createAt: roomTable.createAt?.toJSON() ?? null,
      updateAt: roomTable.updateAt?.toJSON() ?? null,
      deletedAt: roomTable.deletedAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restRoomTable: RestRoomTable): IRoomTable {
    return {
      ...restRoomTable,
      createAt: restRoomTable.createAt ? dayjs(restRoomTable.createAt) : undefined,
      updateAt: restRoomTable.updateAt ? dayjs(restRoomTable.updateAt) : undefined,
      deletedAt: restRoomTable.deletedAt ? dayjs(restRoomTable.deletedAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestRoomTable>): HttpResponse<IRoomTable> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestRoomTable[]>): HttpResponse<IRoomTable[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
