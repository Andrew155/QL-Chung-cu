import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDonationTable } from '../donation-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../donation-table.test-samples';

import { DonationTableService, RestDonationTable } from './donation-table.service';

const requireRestSample: RestDonationTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('DonationTable Service', () => {
  let service: DonationTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IDonationTable | IDonationTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DonationTableService);
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

    it('should create a DonationTable', () => {
      const donationTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(donationTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DonationTable', () => {
      const donationTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(donationTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DonationTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DonationTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DonationTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDonationTableToCollectionIfMissing', () => {
      it('should add a DonationTable to an empty array', () => {
        const donationTable: IDonationTable = sampleWithRequiredData;
        expectedResult = service.addDonationTableToCollectionIfMissing([], donationTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donationTable);
      });

      it('should not add a DonationTable to an array that contains it', () => {
        const donationTable: IDonationTable = sampleWithRequiredData;
        const donationTableCollection: IDonationTable[] = [
          {
            ...donationTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDonationTableToCollectionIfMissing(donationTableCollection, donationTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DonationTable to an array that doesn't contain it", () => {
        const donationTable: IDonationTable = sampleWithRequiredData;
        const donationTableCollection: IDonationTable[] = [sampleWithPartialData];
        expectedResult = service.addDonationTableToCollectionIfMissing(donationTableCollection, donationTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donationTable);
      });

      it('should add only unique DonationTable to an array', () => {
        const donationTableArray: IDonationTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const donationTableCollection: IDonationTable[] = [sampleWithRequiredData];
        expectedResult = service.addDonationTableToCollectionIfMissing(donationTableCollection, ...donationTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const donationTable: IDonationTable = sampleWithRequiredData;
        const donationTable2: IDonationTable = sampleWithPartialData;
        expectedResult = service.addDonationTableToCollectionIfMissing([], donationTable, donationTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(donationTable);
        expect(expectedResult).toContain(donationTable2);
      });

      it('should accept null and undefined values', () => {
        const donationTable: IDonationTable = sampleWithRequiredData;
        expectedResult = service.addDonationTableToCollectionIfMissing([], null, donationTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(donationTable);
      });

      it('should return initial array if no DonationTable is added', () => {
        const donationTableCollection: IDonationTable[] = [sampleWithRequiredData];
        expectedResult = service.addDonationTableToCollectionIfMissing(donationTableCollection, undefined, null);
        expect(expectedResult).toEqual(donationTableCollection);
      });
    });

    describe('compareDonationTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDonationTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDonationTable(entity1, entity2);
        const compareResult2 = service.compareDonationTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDonationTable(entity1, entity2);
        const compareResult2 = service.compareDonationTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDonationTable(entity1, entity2);
        const compareResult2 = service.compareDonationTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
