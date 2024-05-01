import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICitizenTable } from '../citizen-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../citizen-table.test-samples';

import { CitizenTableService, RestCitizenTable } from './citizen-table.service';

const requireRestSample: RestCitizenTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('CitizenTable Service', () => {
  let service: CitizenTableService;
  let httpMock: HttpTestingController;
  let expectedResult: ICitizenTable | ICitizenTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CitizenTableService);
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

    it('should create a CitizenTable', () => {
      const citizenTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(citizenTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CitizenTable', () => {
      const citizenTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(citizenTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CitizenTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CitizenTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CitizenTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCitizenTableToCollectionIfMissing', () => {
      it('should add a CitizenTable to an empty array', () => {
        const citizenTable: ICitizenTable = sampleWithRequiredData;
        expectedResult = service.addCitizenTableToCollectionIfMissing([], citizenTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citizenTable);
      });

      it('should not add a CitizenTable to an array that contains it', () => {
        const citizenTable: ICitizenTable = sampleWithRequiredData;
        const citizenTableCollection: ICitizenTable[] = [
          {
            ...citizenTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCitizenTableToCollectionIfMissing(citizenTableCollection, citizenTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CitizenTable to an array that doesn't contain it", () => {
        const citizenTable: ICitizenTable = sampleWithRequiredData;
        const citizenTableCollection: ICitizenTable[] = [sampleWithPartialData];
        expectedResult = service.addCitizenTableToCollectionIfMissing(citizenTableCollection, citizenTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citizenTable);
      });

      it('should add only unique CitizenTable to an array', () => {
        const citizenTableArray: ICitizenTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const citizenTableCollection: ICitizenTable[] = [sampleWithRequiredData];
        expectedResult = service.addCitizenTableToCollectionIfMissing(citizenTableCollection, ...citizenTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const citizenTable: ICitizenTable = sampleWithRequiredData;
        const citizenTable2: ICitizenTable = sampleWithPartialData;
        expectedResult = service.addCitizenTableToCollectionIfMissing([], citizenTable, citizenTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(citizenTable);
        expect(expectedResult).toContain(citizenTable2);
      });

      it('should accept null and undefined values', () => {
        const citizenTable: ICitizenTable = sampleWithRequiredData;
        expectedResult = service.addCitizenTableToCollectionIfMissing([], null, citizenTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(citizenTable);
      });

      it('should return initial array if no CitizenTable is added', () => {
        const citizenTableCollection: ICitizenTable[] = [sampleWithRequiredData];
        expectedResult = service.addCitizenTableToCollectionIfMissing(citizenTableCollection, undefined, null);
        expect(expectedResult).toEqual(citizenTableCollection);
      });
    });

    describe('compareCitizenTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCitizenTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCitizenTable(entity1, entity2);
        const compareResult2 = service.compareCitizenTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCitizenTable(entity1, entity2);
        const compareResult2 = service.compareCitizenTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCitizenTable(entity1, entity2);
        const compareResult2 = service.compareCitizenTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
