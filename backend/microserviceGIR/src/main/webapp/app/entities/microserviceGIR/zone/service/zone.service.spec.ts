import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IZone } from '../zone.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../zone.test-samples';

import { ZoneService } from './zone.service';

const requireRestSample: IZone = {
  ...sampleWithRequiredData,
};

describe('Zone Service', () => {
  let service: ZoneService;
  let httpMock: HttpTestingController;
  let expectedResult: IZone | IZone[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ZoneService);
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

    it('should create a Zone', () => {
      const zone = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(zone).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Zone', () => {
      const zone = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(zone).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Zone', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Zone', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Zone', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Zone', () => {
      const queryObject: any = {
        page: 0,
        size: 20,
        query: '',
        sort: [],
      };
      service.search(queryObject).subscribe(() => expectedResult);

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(null, { status: 500, statusText: 'Internal Server Error' });
      expect(expectedResult).toBe(null);
    });

    describe('addZoneToCollectionIfMissing', () => {
      it('should add a Zone to an empty array', () => {
        const zone: IZone = sampleWithRequiredData;
        expectedResult = service.addZoneToCollectionIfMissing([], zone);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zone);
      });

      it('should not add a Zone to an array that contains it', () => {
        const zone: IZone = sampleWithRequiredData;
        const zoneCollection: IZone[] = [
          {
            ...zone,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, zone);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Zone to an array that doesn't contain it", () => {
        const zone: IZone = sampleWithRequiredData;
        const zoneCollection: IZone[] = [sampleWithPartialData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, zone);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zone);
      });

      it('should add only unique Zone to an array', () => {
        const zoneArray: IZone[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const zoneCollection: IZone[] = [sampleWithRequiredData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, ...zoneArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const zone: IZone = sampleWithRequiredData;
        const zone2: IZone = sampleWithPartialData;
        expectedResult = service.addZoneToCollectionIfMissing([], zone, zone2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(zone);
        expect(expectedResult).toContain(zone2);
      });

      it('should accept null and undefined values', () => {
        const zone: IZone = sampleWithRequiredData;
        expectedResult = service.addZoneToCollectionIfMissing([], null, zone, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(zone);
      });

      it('should return initial array if no Zone is added', () => {
        const zoneCollection: IZone[] = [sampleWithRequiredData];
        expectedResult = service.addZoneToCollectionIfMissing(zoneCollection, undefined, null);
        expect(expectedResult).toEqual(zoneCollection);
      });
    });

    describe('compareZone', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareZone(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareZone(entity1, entity2);
        const compareResult2 = service.compareZone(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
