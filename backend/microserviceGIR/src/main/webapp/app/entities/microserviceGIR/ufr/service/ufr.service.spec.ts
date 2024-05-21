import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUFR } from '../ufr.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ufr.test-samples';

import { UFRService } from './ufr.service';

const requireRestSample: IUFR = {
  ...sampleWithRequiredData,
};

describe('UFR Service', () => {
  let service: UFRService;
  let httpMock: HttpTestingController;
  let expectedResult: IUFR | IUFR[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UFRService);
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

    it('should create a UFR', () => {
      const uFR = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(uFR).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UFR', () => {
      const uFR = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(uFR).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UFR', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UFR', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UFR', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUFRToCollectionIfMissing', () => {
      it('should add a UFR to an empty array', () => {
        const uFR: IUFR = sampleWithRequiredData;
        expectedResult = service.addUFRToCollectionIfMissing([], uFR);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uFR);
      });

      it('should not add a UFR to an array that contains it', () => {
        const uFR: IUFR = sampleWithRequiredData;
        const uFRCollection: IUFR[] = [
          {
            ...uFR,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUFRToCollectionIfMissing(uFRCollection, uFR);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UFR to an array that doesn't contain it", () => {
        const uFR: IUFR = sampleWithRequiredData;
        const uFRCollection: IUFR[] = [sampleWithPartialData];
        expectedResult = service.addUFRToCollectionIfMissing(uFRCollection, uFR);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uFR);
      });

      it('should add only unique UFR to an array', () => {
        const uFRArray: IUFR[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const uFRCollection: IUFR[] = [sampleWithRequiredData];
        expectedResult = service.addUFRToCollectionIfMissing(uFRCollection, ...uFRArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const uFR: IUFR = sampleWithRequiredData;
        const uFR2: IUFR = sampleWithPartialData;
        expectedResult = service.addUFRToCollectionIfMissing([], uFR, uFR2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(uFR);
        expect(expectedResult).toContain(uFR2);
      });

      it('should accept null and undefined values', () => {
        const uFR: IUFR = sampleWithRequiredData;
        expectedResult = service.addUFRToCollectionIfMissing([], null, uFR, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(uFR);
      });

      it('should return initial array if no UFR is added', () => {
        const uFRCollection: IUFR[] = [sampleWithRequiredData];
        expectedResult = service.addUFRToCollectionIfMissing(uFRCollection, undefined, null);
        expect(expectedResult).toEqual(uFRCollection);
      });
    });

    describe('compareUFR', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUFR(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUFR(entity1, entity2);
        const compareResult2 = service.compareUFR(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUFR(entity1, entity2);
        const compareResult2 = service.compareUFR(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUFR(entity1, entity2);
        const compareResult2 = service.compareUFR(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
