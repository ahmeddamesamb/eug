import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFormationValide } from '../formation-valide.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../formation-valide.test-samples';

import { FormationValideService, RestFormationValide } from './formation-valide.service';

const requireRestSample: RestFormationValide = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('FormationValide Service', () => {
  let service: FormationValideService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormationValide | IFormationValide[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationValideService);
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

    it('should create a FormationValide', () => {
      const formationValide = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formationValide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationValide', () => {
      const formationValide = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formationValide).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationValide', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationValide', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormationValide', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFormationValideToCollectionIfMissing', () => {
      it('should add a FormationValide to an empty array', () => {
        const formationValide: IFormationValide = sampleWithRequiredData;
        expectedResult = service.addFormationValideToCollectionIfMissing([], formationValide);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationValide);
      });

      it('should not add a FormationValide to an array that contains it', () => {
        const formationValide: IFormationValide = sampleWithRequiredData;
        const formationValideCollection: IFormationValide[] = [
          {
            ...formationValide,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormationValideToCollectionIfMissing(formationValideCollection, formationValide);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationValide to an array that doesn't contain it", () => {
        const formationValide: IFormationValide = sampleWithRequiredData;
        const formationValideCollection: IFormationValide[] = [sampleWithPartialData];
        expectedResult = service.addFormationValideToCollectionIfMissing(formationValideCollection, formationValide);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationValide);
      });

      it('should add only unique FormationValide to an array', () => {
        const formationValideArray: IFormationValide[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formationValideCollection: IFormationValide[] = [sampleWithRequiredData];
        expectedResult = service.addFormationValideToCollectionIfMissing(formationValideCollection, ...formationValideArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationValide: IFormationValide = sampleWithRequiredData;
        const formationValide2: IFormationValide = sampleWithPartialData;
        expectedResult = service.addFormationValideToCollectionIfMissing([], formationValide, formationValide2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationValide);
        expect(expectedResult).toContain(formationValide2);
      });

      it('should accept null and undefined values', () => {
        const formationValide: IFormationValide = sampleWithRequiredData;
        expectedResult = service.addFormationValideToCollectionIfMissing([], null, formationValide, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationValide);
      });

      it('should return initial array if no FormationValide is added', () => {
        const formationValideCollection: IFormationValide[] = [sampleWithRequiredData];
        expectedResult = service.addFormationValideToCollectionIfMissing(formationValideCollection, undefined, null);
        expect(expectedResult).toEqual(formationValideCollection);
      });
    });

    describe('compareFormationValide', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormationValide(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormationValide(entity1, entity2);
        const compareResult2 = service.compareFormationValide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormationValide(entity1, entity2);
        const compareResult2 = service.compareFormationValide(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormationValide(entity1, entity2);
        const compareResult2 = service.compareFormationValide(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
