import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeliberation } from '../deliberation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../deliberation.test-samples';

import { DeliberationService } from './deliberation.service';

const requireRestSample: IDeliberation = {
  ...sampleWithRequiredData,
};

describe('Deliberation Service', () => {
  let service: DeliberationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeliberation | IDeliberation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeliberationService);
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

    it('should create a Deliberation', () => {
      const deliberation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(deliberation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Deliberation', () => {
      const deliberation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(deliberation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Deliberation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Deliberation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Deliberation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDeliberationToCollectionIfMissing', () => {
      it('should add a Deliberation to an empty array', () => {
        const deliberation: IDeliberation = sampleWithRequiredData;
        expectedResult = service.addDeliberationToCollectionIfMissing([], deliberation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliberation);
      });

      it('should not add a Deliberation to an array that contains it', () => {
        const deliberation: IDeliberation = sampleWithRequiredData;
        const deliberationCollection: IDeliberation[] = [
          {
            ...deliberation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, deliberation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Deliberation to an array that doesn't contain it", () => {
        const deliberation: IDeliberation = sampleWithRequiredData;
        const deliberationCollection: IDeliberation[] = [sampleWithPartialData];
        expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, deliberation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliberation);
      });

      it('should add only unique Deliberation to an array', () => {
        const deliberationArray: IDeliberation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const deliberationCollection: IDeliberation[] = [sampleWithRequiredData];
        expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, ...deliberationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deliberation: IDeliberation = sampleWithRequiredData;
        const deliberation2: IDeliberation = sampleWithPartialData;
        expectedResult = service.addDeliberationToCollectionIfMissing([], deliberation, deliberation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deliberation);
        expect(expectedResult).toContain(deliberation2);
      });

      it('should accept null and undefined values', () => {
        const deliberation: IDeliberation = sampleWithRequiredData;
        expectedResult = service.addDeliberationToCollectionIfMissing([], null, deliberation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deliberation);
      });

      it('should return initial array if no Deliberation is added', () => {
        const deliberationCollection: IDeliberation[] = [sampleWithRequiredData];
        expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, undefined, null);
        expect(expectedResult).toEqual(deliberationCollection);
      });
    });

    describe('compareDeliberation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeliberation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeliberation(entity1, entity2);
        const compareResult2 = service.compareDeliberation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeliberation(entity1, entity2);
        const compareResult2 = service.compareDeliberation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeliberation(entity1, entity2);
        const compareResult2 = service.compareDeliberation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
