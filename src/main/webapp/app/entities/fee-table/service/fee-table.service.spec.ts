import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFeeTable } from '../fee-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../fee-table.test-samples';

import { FeeTableService, RestFeeTable } from './fee-table.service';

const requireRestSample: RestFeeTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('FeeTable Service', () => {
  let service: FeeTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IFeeTable | IFeeTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FeeTableService);
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

    it('should create a FeeTable', () => {
      const feeTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(feeTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FeeTable', () => {
      const feeTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(feeTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FeeTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FeeTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FeeTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFeeTableToCollectionIfMissing', () => {
      it('should add a FeeTable to an empty array', () => {
        const feeTable: IFeeTable = sampleWithRequiredData;
        expectedResult = service.addFeeTableToCollectionIfMissing([], feeTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feeTable);
      });

      it('should not add a FeeTable to an array that contains it', () => {
        const feeTable: IFeeTable = sampleWithRequiredData;
        const feeTableCollection: IFeeTable[] = [
          {
            ...feeTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFeeTableToCollectionIfMissing(feeTableCollection, feeTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FeeTable to an array that doesn't contain it", () => {
        const feeTable: IFeeTable = sampleWithRequiredData;
        const feeTableCollection: IFeeTable[] = [sampleWithPartialData];
        expectedResult = service.addFeeTableToCollectionIfMissing(feeTableCollection, feeTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feeTable);
      });

      it('should add only unique FeeTable to an array', () => {
        const feeTableArray: IFeeTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const feeTableCollection: IFeeTable[] = [sampleWithRequiredData];
        expectedResult = service.addFeeTableToCollectionIfMissing(feeTableCollection, ...feeTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const feeTable: IFeeTable = sampleWithRequiredData;
        const feeTable2: IFeeTable = sampleWithPartialData;
        expectedResult = service.addFeeTableToCollectionIfMissing([], feeTable, feeTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(feeTable);
        expect(expectedResult).toContain(feeTable2);
      });

      it('should accept null and undefined values', () => {
        const feeTable: IFeeTable = sampleWithRequiredData;
        expectedResult = service.addFeeTableToCollectionIfMissing([], null, feeTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(feeTable);
      });

      it('should return initial array if no FeeTable is added', () => {
        const feeTableCollection: IFeeTable[] = [sampleWithRequiredData];
        expectedResult = service.addFeeTableToCollectionIfMissing(feeTableCollection, undefined, null);
        expect(expectedResult).toEqual(feeTableCollection);
      });
    });

    describe('compareFeeTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFeeTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFeeTable(entity1, entity2);
        const compareResult2 = service.compareFeeTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFeeTable(entity1, entity2);
        const compareResult2 = service.compareFeeTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFeeTable(entity1, entity2);
        const compareResult2 = service.compareFeeTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
