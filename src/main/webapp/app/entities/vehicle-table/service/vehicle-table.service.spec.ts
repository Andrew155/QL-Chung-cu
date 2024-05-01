import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVehicleTable } from '../vehicle-table.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../vehicle-table.test-samples';

import { VehicleTableService, RestVehicleTable } from './vehicle-table.service';

const requireRestSample: RestVehicleTable = {
  ...sampleWithRequiredData,
  createAt: sampleWithRequiredData.createAt?.toJSON(),
  updateAt: sampleWithRequiredData.updateAt?.toJSON(),
  deletedAt: sampleWithRequiredData.deletedAt?.toJSON(),
};

describe('VehicleTable Service', () => {
  let service: VehicleTableService;
  let httpMock: HttpTestingController;
  let expectedResult: IVehicleTable | IVehicleTable[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleTableService);
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

    it('should create a VehicleTable', () => {
      const vehicleTable = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(vehicleTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleTable', () => {
      const vehicleTable = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(vehicleTable).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleTable', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleTable', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a VehicleTable', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addVehicleTableToCollectionIfMissing', () => {
      it('should add a VehicleTable to an empty array', () => {
        const vehicleTable: IVehicleTable = sampleWithRequiredData;
        expectedResult = service.addVehicleTableToCollectionIfMissing([], vehicleTable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleTable);
      });

      it('should not add a VehicleTable to an array that contains it', () => {
        const vehicleTable: IVehicleTable = sampleWithRequiredData;
        const vehicleTableCollection: IVehicleTable[] = [
          {
            ...vehicleTable,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addVehicleTableToCollectionIfMissing(vehicleTableCollection, vehicleTable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleTable to an array that doesn't contain it", () => {
        const vehicleTable: IVehicleTable = sampleWithRequiredData;
        const vehicleTableCollection: IVehicleTable[] = [sampleWithPartialData];
        expectedResult = service.addVehicleTableToCollectionIfMissing(vehicleTableCollection, vehicleTable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleTable);
      });

      it('should add only unique VehicleTable to an array', () => {
        const vehicleTableArray: IVehicleTable[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const vehicleTableCollection: IVehicleTable[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleTableToCollectionIfMissing(vehicleTableCollection, ...vehicleTableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleTable: IVehicleTable = sampleWithRequiredData;
        const vehicleTable2: IVehicleTable = sampleWithPartialData;
        expectedResult = service.addVehicleTableToCollectionIfMissing([], vehicleTable, vehicleTable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleTable);
        expect(expectedResult).toContain(vehicleTable2);
      });

      it('should accept null and undefined values', () => {
        const vehicleTable: IVehicleTable = sampleWithRequiredData;
        expectedResult = service.addVehicleTableToCollectionIfMissing([], null, vehicleTable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleTable);
      });

      it('should return initial array if no VehicleTable is added', () => {
        const vehicleTableCollection: IVehicleTable[] = [sampleWithRequiredData];
        expectedResult = service.addVehicleTableToCollectionIfMissing(vehicleTableCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleTableCollection);
      });
    });

    describe('compareVehicleTable', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareVehicleTable(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareVehicleTable(entity1, entity2);
        const compareResult2 = service.compareVehicleTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareVehicleTable(entity1, entity2);
        const compareResult2 = service.compareVehicleTable(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareVehicleTable(entity1, entity2);
        const compareResult2 = service.compareVehicleTable(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
