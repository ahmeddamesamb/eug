import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRessourceAide } from '../ressource-aide.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ressource-aide.test-samples';

import { RessourceAideService } from './ressource-aide.service';

const requireRestSample: IRessourceAide = {
  ...sampleWithRequiredData,
};

describe('RessourceAide Service', () => {
  let service: RessourceAideService;
  let httpMock: HttpTestingController;
  let expectedResult: IRessourceAide | IRessourceAide[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RessourceAideService);
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

    it('should create a RessourceAide', () => {
      const ressourceAide = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ressourceAide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RessourceAide', () => {
      const ressourceAide = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ressourceAide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RessourceAide', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RessourceAide', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a RessourceAide', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a RessourceAide', () => {
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

    describe('addRessourceAideToCollectionIfMissing', () => {
      it('should add a RessourceAide to an empty array', () => {
        const ressourceAide: IRessourceAide = sampleWithRequiredData;
        expectedResult = service.addRessourceAideToCollectionIfMissing([], ressourceAide);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ressourceAide);
      });

      it('should not add a RessourceAide to an array that contains it', () => {
        const ressourceAide: IRessourceAide = sampleWithRequiredData;
        const ressourceAideCollection: IRessourceAide[] = [
          {
            ...ressourceAide,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addRessourceAideToCollectionIfMissing(ressourceAideCollection, ressourceAide);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RessourceAide to an array that doesn't contain it", () => {
        const ressourceAide: IRessourceAide = sampleWithRequiredData;
        const ressourceAideCollection: IRessourceAide[] = [sampleWithPartialData];
        expectedResult = service.addRessourceAideToCollectionIfMissing(ressourceAideCollection, ressourceAide);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ressourceAide);
      });

      it('should add only unique RessourceAide to an array', () => {
        const ressourceAideArray: IRessourceAide[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ressourceAideCollection: IRessourceAide[] = [sampleWithRequiredData];
        expectedResult = service.addRessourceAideToCollectionIfMissing(ressourceAideCollection, ...ressourceAideArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ressourceAide: IRessourceAide = sampleWithRequiredData;
        const ressourceAide2: IRessourceAide = sampleWithPartialData;
        expectedResult = service.addRessourceAideToCollectionIfMissing([], ressourceAide, ressourceAide2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ressourceAide);
        expect(expectedResult).toContain(ressourceAide2);
      });

      it('should accept null and undefined values', () => {
        const ressourceAide: IRessourceAide = sampleWithRequiredData;
        expectedResult = service.addRessourceAideToCollectionIfMissing([], null, ressourceAide, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ressourceAide);
      });

      it('should return initial array if no RessourceAide is added', () => {
        const ressourceAideCollection: IRessourceAide[] = [sampleWithRequiredData];
        expectedResult = service.addRessourceAideToCollectionIfMissing(ressourceAideCollection, undefined, null);
        expect(expectedResult).toEqual(ressourceAideCollection);
      });
    });

    describe('compareRessourceAide', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareRessourceAide(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareRessourceAide(entity1, entity2);
        const compareResult2 = service.compareRessourceAide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareRessourceAide(entity1, entity2);
        const compareResult2 = service.compareRessourceAide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareRessourceAide(entity1, entity2);
        const compareResult2 = service.compareRessourceAide(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
