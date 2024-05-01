import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INotificationTable } from '../notification-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../notification-table.test-samples';

import { NotificationTableService, RestNotificationTable } from './notification-table.service';

const requireRestSample: RestNotificationTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('NotificationTable Service', () => {
  let service: NotificationTableService;
  let httpMock: HttpTestingController;
  let expectedResult: INotificationTable | INotificationTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotificationTableService);
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

    it('should create a NotificationTable', () => {
      const notificationTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(notificationTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NotificationTable', () => {
      const notificationTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(notificationTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NotificationTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NotificationTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a NotificationTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addNotificationTableToCollectionIfMissing', () => {
      it('should add a NotificationTable to an empty array', () => {
        const notificationTable: INotificationTable = sampleWithRequiredData;
        expectedResult = service.addNotificationTableToCollectionIfMissing([], notificationTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationTable);
      });

      it('should not add a NotificationTable to an array that contains it', () => {
        const notificationTable: INotificationTable = sampleWithRequiredData;
        const notificationTableCollection: INotificationTable[] = [
          {
            ...notificationTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addNotificationTableToCollectionIfMissing(notificationTableCollection, notificationTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NotificationTable to an array that doesn't contain it", () => {
        const notificationTable: INotificationTable = sampleWithRequiredData;
        const notificationTableCollection: INotificationTable[] = [sampleWithPartialData];
        expectedResult = service.addNotificationTableToCollectionIfMissing(notificationTableCollection, notificationTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationTable);
      });

      it('should add only unique NotificationTable to an array', () => {
        const notificationTableArray: INotificationTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const notificationTableCollection: INotificationTable[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationTableToCollectionIfMissing(notificationTableCollection, ...notificationTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificationTable: INotificationTable = sampleWithRequiredData;
        const notificationTable2: INotificationTable = sampleWithPartialData;
        expectedResult = service.addNotificationTableToCollectionIfMissing([], notificationTable, notificationTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificationTable);
        expect(expectedResult).toContain(notificationTable2);
      });

      it('should accept null and undefined values', () => {
        const notificationTable: INotificationTable = sampleWithRequiredData;
        expectedResult = service.addNotificationTableToCollectionIfMissing([], null, notificationTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificationTable);
      });

      it('should return initial array if no NotificationTable is added', () => {
        const notificationTableCollection: INotificationTable[] = [sampleWithRequiredData];
        expectedResult = service.addNotificationTableToCollectionIfMissing(notificationTableCollection, undefined, null);
        expect(expectedResult).toEqual(notificationTableCollection);
      });
    });

    describe('compareNotificationTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareNotificationTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareNotificationTable(entity1, entity2);
        const compareResult2 = service.compareNotificationTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareNotificationTable(entity1, entity2);
        const compareResult2 = service.compareNotificationTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareNotificationTable(entity1, entity2);
        const compareResult2 = service.compareNotificationTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
