import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInscriptionAdministrative } from '../inscription-administrative.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../inscription-administrative.test-samples';

import { InscriptionAdministrativeService } from './inscription-administrative.service';

const requireRestSample: IInscriptionAdministrative = {
  ...sampleWithRequiredData,
};

describe('InscriptionAdministrative Service', () => {
  let service: InscriptionAdministrativeService;
  let httpMock: HttpTestingController;
  let expectedResult: IInscriptionAdministrative | IInscriptionAdministrative[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InscriptionAdministrativeService);
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

    it('should create a InscriptionAdministrative', () => {
      const inscriptionAdministrative = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InscriptionAdministrative', () => {
      const inscriptionAdministrative = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InscriptionAdministrative', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InscriptionAdministrative', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InscriptionAdministrative', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InscriptionAdministrative', () => {
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

    describe('addInscriptionAdministrativeToCollectionIfMissing', () => {
      it('should add a InscriptionAdministrative to an empty array', () => {
        const inscriptionAdministrative: IInscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing([], inscriptionAdministrative);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionAdministrative);
      });

      it('should not add a InscriptionAdministrative to an array that contains it', () => {
        const inscriptionAdministrative: IInscriptionAdministrative = sampleWithRequiredData;
        const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [
          {
            ...inscriptionAdministrative,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing(
          inscriptionAdministrativeCollection,
          inscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InscriptionAdministrative to an array that doesn't contain it", () => {
        const inscriptionAdministrative: IInscriptionAdministrative = sampleWithRequiredData;
        const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [sampleWithPartialData];
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing(
          inscriptionAdministrativeCollection,
          inscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionAdministrative);
      });

      it('should add only unique InscriptionAdministrative to an array', () => {
        const inscriptionAdministrativeArray: IInscriptionAdministrative[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing(
          inscriptionAdministrativeCollection,
          ...inscriptionAdministrativeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscriptionAdministrative: IInscriptionAdministrative = sampleWithRequiredData;
        const inscriptionAdministrative2: IInscriptionAdministrative = sampleWithPartialData;
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing(
          [],
          inscriptionAdministrative,
          inscriptionAdministrative2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionAdministrative);
        expect(expectedResult).toContain(inscriptionAdministrative2);
      });

      it('should accept null and undefined values', () => {
        const inscriptionAdministrative: IInscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing([], null, inscriptionAdministrative, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionAdministrative);
      });

      it('should return initial array if no InscriptionAdministrative is added', () => {
        const inscriptionAdministrativeCollection: IInscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionAdministrativeToCollectionIfMissing(inscriptionAdministrativeCollection, undefined, null);
        expect(expectedResult).toEqual(inscriptionAdministrativeCollection);
      });
    });

    describe('compareInscriptionAdministrative', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInscriptionAdministrative(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
