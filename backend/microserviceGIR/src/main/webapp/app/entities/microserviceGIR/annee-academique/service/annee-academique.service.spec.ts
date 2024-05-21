import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnneeAcademique } from '../annee-academique.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../annee-academique.test-samples';

import { AnneeAcademiqueService } from './annee-academique.service';

const requireRestSample: IAnneeAcademique = {
  ...sampleWithRequiredData,
};

describe('AnneeAcademique Service', () => {
  let service: AnneeAcademiqueService;
  let httpMock: HttpTestingController;
  let expectedResult: IAnneeAcademique | IAnneeAcademique[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnneeAcademiqueService);
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

    it('should create a AnneeAcademique', () => {
      const anneeAcademique = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(anneeAcademique).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AnneeAcademique', () => {
      const anneeAcademique = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(anneeAcademique).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AnneeAcademique', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AnneeAcademique', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AnneeAcademique', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAnneeAcademiqueToCollectionIfMissing', () => {
      it('should add a AnneeAcademique to an empty array', () => {
        const anneeAcademique: IAnneeAcademique = sampleWithRequiredData;
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], anneeAcademique);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anneeAcademique);
      });

      it('should not add a AnneeAcademique to an array that contains it', () => {
        const anneeAcademique: IAnneeAcademique = sampleWithRequiredData;
        const anneeAcademiqueCollection: IAnneeAcademique[] = [
          {
            ...anneeAcademique,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, anneeAcademique);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AnneeAcademique to an array that doesn't contain it", () => {
        const anneeAcademique: IAnneeAcademique = sampleWithRequiredData;
        const anneeAcademiqueCollection: IAnneeAcademique[] = [sampleWithPartialData];
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, anneeAcademique);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anneeAcademique);
      });

      it('should add only unique AnneeAcademique to an array', () => {
        const anneeAcademiqueArray: IAnneeAcademique[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const anneeAcademiqueCollection: IAnneeAcademique[] = [sampleWithRequiredData];
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, ...anneeAcademiqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const anneeAcademique: IAnneeAcademique = sampleWithRequiredData;
        const anneeAcademique2: IAnneeAcademique = sampleWithPartialData;
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], anneeAcademique, anneeAcademique2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(anneeAcademique);
        expect(expectedResult).toContain(anneeAcademique2);
      });

      it('should accept null and undefined values', () => {
        const anneeAcademique: IAnneeAcademique = sampleWithRequiredData;
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], null, anneeAcademique, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(anneeAcademique);
      });

      it('should return initial array if no AnneeAcademique is added', () => {
        const anneeAcademiqueCollection: IAnneeAcademique[] = [sampleWithRequiredData];
        expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, undefined, null);
        expect(expectedResult).toEqual(anneeAcademiqueCollection);
      });
    });

    describe('compareAnneeAcademique', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAnneeAcademique(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAnneeAcademique(entity1, entity2);
        const compareResult2 = service.compareAnneeAcademique(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAnneeAcademique(entity1, entity2);
        const compareResult2 = service.compareAnneeAcademique(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAnneeAcademique(entity1, entity2);
        const compareResult2 = service.compareAnneeAcademique(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
