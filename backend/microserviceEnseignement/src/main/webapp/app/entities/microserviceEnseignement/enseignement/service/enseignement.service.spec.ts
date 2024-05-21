import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnseignement } from '../enseignement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../enseignement.test-samples';

import { EnseignementService } from './enseignement.service';

const requireRestSample: IEnseignement = {
  ...sampleWithRequiredData,
};

describe('Enseignement Service', () => {
  let service: EnseignementService;
  let httpMock: HttpTestingController;
  let expectedResult: IEnseignement | IEnseignement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EnseignementService);
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

    it('should create a Enseignement', () => {
      const enseignement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(enseignement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Enseignement', () => {
      const enseignement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(enseignement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Enseignement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Enseignement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Enseignement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEnseignementToCollectionIfMissing', () => {
      it('should add a Enseignement to an empty array', () => {
        const enseignement: IEnseignement = sampleWithRequiredData;
        expectedResult = service.addEnseignementToCollectionIfMissing([], enseignement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enseignement);
      });

      it('should not add a Enseignement to an array that contains it', () => {
        const enseignement: IEnseignement = sampleWithRequiredData;
        const enseignementCollection: IEnseignement[] = [
          {
            ...enseignement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEnseignementToCollectionIfMissing(enseignementCollection, enseignement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Enseignement to an array that doesn't contain it", () => {
        const enseignement: IEnseignement = sampleWithRequiredData;
        const enseignementCollection: IEnseignement[] = [sampleWithPartialData];
        expectedResult = service.addEnseignementToCollectionIfMissing(enseignementCollection, enseignement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enseignement);
      });

      it('should add only unique Enseignement to an array', () => {
        const enseignementArray: IEnseignement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const enseignementCollection: IEnseignement[] = [sampleWithRequiredData];
        expectedResult = service.addEnseignementToCollectionIfMissing(enseignementCollection, ...enseignementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const enseignement: IEnseignement = sampleWithRequiredData;
        const enseignement2: IEnseignement = sampleWithPartialData;
        expectedResult = service.addEnseignementToCollectionIfMissing([], enseignement, enseignement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(enseignement);
        expect(expectedResult).toContain(enseignement2);
      });

      it('should accept null and undefined values', () => {
        const enseignement: IEnseignement = sampleWithRequiredData;
        expectedResult = service.addEnseignementToCollectionIfMissing([], null, enseignement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(enseignement);
      });

      it('should return initial array if no Enseignement is added', () => {
        const enseignementCollection: IEnseignement[] = [sampleWithRequiredData];
        expectedResult = service.addEnseignementToCollectionIfMissing(enseignementCollection, undefined, null);
        expect(expectedResult).toEqual(enseignementCollection);
      });
    });

    describe('compareEnseignement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEnseignement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareEnseignement(entity1, entity2);
        const compareResult2 = service.compareEnseignement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareEnseignement(entity1, entity2);
        const compareResult2 = service.compareEnseignement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareEnseignement(entity1, entity2);
        const compareResult2 = service.compareEnseignement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
