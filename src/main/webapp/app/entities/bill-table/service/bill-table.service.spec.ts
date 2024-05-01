import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBillTable } from '../bill-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bill-table.test-samples';

import { BillTableService, RestBillTable } from './bill-table.service';

const requireRestSample: RestBillTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('BillTable Service', () => {
  let service: BillTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IBillTable | IBillTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BillTableService);
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

    it('should create a BillTable', () => {
      const billTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(billTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BillTable', () => {
      const billTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(billTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BillTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BillTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BillTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBillTableToCollectionIfMissing', () => {
      it('should add a BillTable to an empty array', () => {
        const billTable: IBillTable = sampleWithRequiredData;
        expectedResult = service.addBillTableToCollectionIfMissing([], billTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(billTable);
      });

      it('should not add a BillTable to an array that contains it', () => {
        const billTable: IBillTable = sampleWithRequiredData;
        const billTableCollection: IBillTable[] = [
          {
            ...billTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBillTableToCollectionIfMissing(billTableCollection, billTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BillTable to an array that doesn't contain it", () => {
        const billTable: IBillTable = sampleWithRequiredData;
        const billTableCollection: IBillTable[] = [sampleWithPartialData];
        expectedResult = service.addBillTableToCollectionIfMissing(billTableCollection, billTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(billTable);
      });

      it('should add only unique BillTable to an array', () => {
        const billTableArray: IBillTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const billTableCollection: IBillTable[] = [sampleWithRequiredData];
        expectedResult = service.addBillTableToCollectionIfMissing(billTableCollection, ...billTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const billTable: IBillTable = sampleWithRequiredData;
        const billTable2: IBillTable = sampleWithPartialData;
        expectedResult = service.addBillTableToCollectionIfMissing([], billTable, billTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(billTable);
        expect(expectedResult).toContain(billTable2);
      });

      it('should accept null and undefined values', () => {
        const billTable: IBillTable = sampleWithRequiredData;
        expectedResult = service.addBillTableToCollectionIfMissing([], null, billTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(billTable);
      });

      it('should return initial array if no BillTable is added', () => {
        const billTableCollection: IBillTable[] = [sampleWithRequiredData];
        expectedResult = service.addBillTableToCollectionIfMissing(billTableCollection, undefined, null);
        expect(expectedResult).toEqual(billTableCollection);
      });
    });

    describe('compareBillTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBillTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBillTable(entity1, entity2);
        const compareResult2 = service.compareBillTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBillTable(entity1, entity2);
        const compareResult2 = service.compareBillTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBillTable(entity1, entity2);
        const compareResult2 = service.compareBillTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
