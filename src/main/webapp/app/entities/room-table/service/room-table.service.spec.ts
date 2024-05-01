import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRoomTable } from '../room-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../room-table.test-samples';

import { RoomTableService, RestRoomTable } from './room-table.service';

const requireRestSample: RestRoomTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('RoomTable Service', () => {
  let service: RoomTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IRoomTable | IRoomTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoomTableService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a RoomTable', () => {
      const roomTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(roomTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RoomTable', () => {
      const roomTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(roomTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RoomTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RoomTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RoomTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRoomTableToCollectionIfMissing', () => {
      it('should add a RoomTable to an empty array', () => {
        const roomTable: IRoomTable = sampleWithRequiredData;
        expectedResult = service.addRoomTableToCollectionIfMissing([], roomTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomTable);
      });

      it('should not add a RoomTable to an array that contains it', () => {
        const roomTable: IRoomTable = sampleWithRequiredData;
        const roomTableCollection: IRoomTable[] = [
          {
            ...roomTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRoomTableToCollectionIfMissing(roomTableCollection, roomTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RoomTable to an array that doesn't contain it", () => {
        const roomTable: IRoomTable = sampleWithRequiredData;
        const roomTableCollection: IRoomTable[] = [sampleWithPartialData];
        expectedResult = service.addRoomTableToCollectionIfMissing(roomTableCollection, roomTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomTable);
      });

      it('should add only unique RoomTable to an array', () => {
        const roomTableArray: IRoomTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const roomTableCollection: IRoomTable[] = [sampleWithRequiredData];
        expectedResult = service.addRoomTableToCollectionIfMissing(roomTableCollection, ...roomTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const roomTable: IRoomTable = sampleWithRequiredData;
        const roomTable2: IRoomTable = sampleWithPartialData;
        expectedResult = service.addRoomTableToCollectionIfMissing([], roomTable, roomTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(roomTable);
        expect(expectedResult).toContain(roomTable2);
      });

      it('should accept null and undefined values', () => {
        const roomTable: IRoomTable = sampleWithRequiredData;
        expectedResult = service.addRoomTableToCollectionIfMissing([], null, roomTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(roomTable);
      });

      it('should return initial array if no RoomTable is added', () => {
        const roomTableCollection: IRoomTable[] = [sampleWithRequiredData];
        expectedResult = service.addRoomTableToCollectionIfMissing(roomTableCollection, undefined, null);
        expect(expectedResult).toEqual(roomTableCollection);
      });
    });

    describe('compareRoomTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRoomTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRoomTable(entity1, entity2);
        const compareResult2 = service.compareRoomTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRoomTable(entity1, entity2);
        const compareResult2 = service.compareRoomTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRoomTable(entity1, entity2);
        const compareResult2 = service.compareRoomTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
