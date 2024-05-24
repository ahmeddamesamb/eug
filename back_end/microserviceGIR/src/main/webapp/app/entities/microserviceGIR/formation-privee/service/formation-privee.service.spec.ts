import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFormationPrivee } from '../formation-privee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../formation-privee.test-samples';

import { FormationPriveeService } from './formation-privee.service';

const requireRestSample: IFormationPrivee = {
  ...sampleWithRequiredData,
};

describe('FormationPrivee Service', () => {
  let service: FormationPriveeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormationPrivee | IFormationPrivee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationPriveeService);
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

    it('should create a FormationPrivee', () => {
      const formationPrivee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formationPrivee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationPrivee', () => {
      const formationPrivee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formationPrivee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationPrivee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationPrivee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormationPrivee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFormationPriveeToCollectionIfMissing', () => {
      it('should add a FormationPrivee to an empty array', () => {
        const formationPrivee: IFormationPrivee = sampleWithRequiredData;
        expectedResult = service.addFormationPriveeToCollectionIfMissing([], formationPrivee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationPrivee);
      });

      it('should not add a FormationPrivee to an array that contains it', () => {
        const formationPrivee: IFormationPrivee = sampleWithRequiredData;
        const formationPriveeCollection: IFormationPrivee[] = [
          {
            ...formationPrivee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormationPriveeToCollectionIfMissing(formationPriveeCollection, formationPrivee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationPrivee to an array that doesn't contain it", () => {
        const formationPrivee: IFormationPrivee = sampleWithRequiredData;
        const formationPriveeCollection: IFormationPrivee[] = [sampleWithPartialData];
        expectedResult = service.addFormationPriveeToCollectionIfMissing(formationPriveeCollection, formationPrivee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationPrivee);
      });

      it('should add only unique FormationPrivee to an array', () => {
        const formationPriveeArray: IFormationPrivee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formationPriveeCollection: IFormationPrivee[] = [sampleWithRequiredData];
        expectedResult = service.addFormationPriveeToCollectionIfMissing(formationPriveeCollection, ...formationPriveeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationPrivee: IFormationPrivee = sampleWithRequiredData;
        const formationPrivee2: IFormationPrivee = sampleWithPartialData;
        expectedResult = service.addFormationPriveeToCollectionIfMissing([], formationPrivee, formationPrivee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationPrivee);
        expect(expectedResult).toContain(formationPrivee2);
      });

      it('should accept null and undefined values', () => {
        const formationPrivee: IFormationPrivee = sampleWithRequiredData;
        expectedResult = service.addFormationPriveeToCollectionIfMissing([], null, formationPrivee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationPrivee);
      });

      it('should return initial array if no FormationPrivee is added', () => {
        const formationPriveeCollection: IFormationPrivee[] = [sampleWithRequiredData];
        expectedResult = service.addFormationPriveeToCollectionIfMissing(formationPriveeCollection, undefined, null);
        expect(expectedResult).toEqual(formationPriveeCollection);
      });
    });

    describe('compareFormationPrivee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormationPrivee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormationPrivee(entity1, entity2);
        const compareResult2 = service.compareFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormationPrivee(entity1, entity2);
        const compareResult2 = service.compareFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormationPrivee(entity1, entity2);
        const compareResult2 = service.compareFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
