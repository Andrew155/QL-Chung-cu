import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRequestTable } from '../request-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../request-table.test-samples';

import { RequestTableService, RestRequestTable } from './request-table.service';

const requireRestSample: RestRequestTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('RequestTable Service', () => {
  let service: RequestTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IRequestTable | IRequestTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RequestTableService);
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

    it('should create a RequestTable', () => {
      const requestTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(requestTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RequestTable', () => {
      const requestTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(requestTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RequestTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RequestTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RequestTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addRequestTableToCollectionIfMissing', () => {
      it('should add a RequestTable to an empty array', () => {
        const requestTable: IRequestTable = sampleWithRequiredData;
        expectedResult = service.addRequestTableToCollectionIfMissing([], requestTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestTable);
      });

      it('should not add a RequestTable to an array that contains it', () => {
        const requestTable: IRequestTable = sampleWithRequiredData;
        const requestTableCollection: IRequestTable[] = [
          {
            ...requestTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRequestTableToCollectionIfMissing(requestTableCollection, requestTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RequestTable to an array that doesn't contain it", () => {
        const requestTable: IRequestTable = sampleWithRequiredData;
        const requestTableCollection: IRequestTable[] = [sampleWithPartialData];
        expectedResult = service.addRequestTableToCollectionIfMissing(requestTableCollection, requestTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestTable);
      });

      it('should add only unique RequestTable to an array', () => {
        const requestTableArray: IRequestTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const requestTableCollection: IRequestTable[] = [sampleWithRequiredData];
        expectedResult = service.addRequestTableToCollectionIfMissing(requestTableCollection, ...requestTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const requestTable: IRequestTable = sampleWithRequiredData;
        const requestTable2: IRequestTable = sampleWithPartialData;
        expectedResult = service.addRequestTableToCollectionIfMissing([], requestTable, requestTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(requestTable);
        expect(expectedResult).toContain(requestTable2);
      });

      it('should accept null and undefined values', () => {
        const requestTable: IRequestTable = sampleWithRequiredData;
        expectedResult = service.addRequestTableToCollectionIfMissing([], null, requestTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(requestTable);
      });

      it('should return initial array if no RequestTable is added', () => {
        const requestTableCollection: IRequestTable[] = [sampleWithRequiredData];
        expectedResult = service.addRequestTableToCollectionIfMissing(requestTableCollection, undefined, null);
        expect(expectedResult).toEqual(requestTableCollection);
      });
    });

    describe('compareRequestTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRequestTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRequestTable(entity1, entity2);
        const compareResult2 = service.compareRequestTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRequestTable(entity1, entity2);
        const compareResult2 = service.compareRequestTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRequestTable(entity1, entity2);
        const compareResult2 = service.compareRequestTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
