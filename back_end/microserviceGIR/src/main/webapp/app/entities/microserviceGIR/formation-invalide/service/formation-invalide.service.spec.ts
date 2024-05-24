import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormationInvalide } from '../formation-invalide.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../formation-invalide.test-samples';

import { FormationInvalideService } from './formation-invalide.service';

const requireRestSample: IFormationInvalide = {
  ...sampleWithRequiredData,
};

describe('FormationInvalide Service', () => {
  let service: FormationInvalideService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormationInvalide | IFormationInvalide[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationInvalideService);
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

    it('should create a FormationInvalide', () => {
      const formationInvalide = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formationInvalide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationInvalide', () => {
      const formationInvalide = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formationInvalide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationInvalide', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationInvalide', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormationInvalide', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFormationInvalideToCollectionIfMissing', () => {
      it('should add a FormationInvalide to an empty array', () => {
        const formationInvalide: IFormationInvalide = sampleWithRequiredData;
        expectedResult = service.addFormationInvalideToCollectionIfMissing([], formationInvalide);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationInvalide);
      });

      it('should not add a FormationInvalide to an array that contains it', () => {
        const formationInvalide: IFormationInvalide = sampleWithRequiredData;
        const formationInvalideCollection: IFormationInvalide[] = [
          {
            ...formationInvalide,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormationInvalideToCollectionIfMissing(formationInvalideCollection, formationInvalide);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationInvalide to an array that doesn't contain it", () => {
        const formationInvalide: IFormationInvalide = sampleWithRequiredData;
        const formationInvalideCollection: IFormationInvalide[] = [sampleWithPartialData];
        expectedResult = service.addFormationInvalideToCollectionIfMissing(formationInvalideCollection, formationInvalide);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationInvalide);
      });

      it('should add only unique FormationInvalide to an array', () => {
        const formationInvalideArray: IFormationInvalide[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formationInvalideCollection: IFormationInvalide[] = [sampleWithRequiredData];
        expectedResult = service.addFormationInvalideToCollectionIfMissing(formationInvalideCollection, ...formationInvalideArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationInvalide: IFormationInvalide = sampleWithRequiredData;
        const formationInvalide2: IFormationInvalide = sampleWithPartialData;
        expectedResult = service.addFormationInvalideToCollectionIfMissing([], formationInvalide, formationInvalide2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationInvalide);
        expect(expectedResult).toContain(formationInvalide2);
      });

      it('should accept null and undefined values', () => {
        const formationInvalide: IFormationInvalide = sampleWithRequiredData;
        expectedResult = service.addFormationInvalideToCollectionIfMissing([], null, formationInvalide, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationInvalide);
      });

      it('should return initial array if no FormationInvalide is added', () => {
        const formationInvalideCollection: IFormationInvalide[] = [sampleWithRequiredData];
        expectedResult = service.addFormationInvalideToCollectionIfMissing(formationInvalideCollection, undefined, null);
        expect(expectedResult).toEqual(formationInvalideCollection);
      });
    });

    describe('compareFormationInvalide', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormationInvalide(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormationInvalide(entity1, entity2);
        const compareResult2 = service.compareFormationInvalide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormationInvalide(entity1, entity2);
        const compareResult2 = service.compareFormationInvalide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormationInvalide(entity1, entity2);
        const compareResult2 = service.compareFormationInvalide(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
