import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeFrais } from '../type-frais.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-frais.test-samples';

import { TypeFraisService } from './type-frais.service';

const requireRestSample: ITypeFrais = {
  ...sampleWithRequiredData,
};

describe('TypeFrais Service', () => {
  let service: TypeFraisService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeFrais | ITypeFrais[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeFraisService);
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

    it('should create a TypeFrais', () => {
      const typeFrais = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeFrais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeFrais', () => {
      const typeFrais = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeFrais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeFrais', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeFrais', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeFrais', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeFraisToCollectionIfMissing', () => {
      it('should add a TypeFrais to an empty array', () => {
        const typeFrais: ITypeFrais = sampleWithRequiredData;
        expectedResult = service.addTypeFraisToCollectionIfMissing([], typeFrais);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeFrais);
      });

      it('should not add a TypeFrais to an array that contains it', () => {
        const typeFrais: ITypeFrais = sampleWithRequiredData;
        const typeFraisCollection: ITypeFrais[] = [
          {
            ...typeFrais,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeFraisToCollectionIfMissing(typeFraisCollection, typeFrais);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeFrais to an array that doesn't contain it", () => {
        const typeFrais: ITypeFrais = sampleWithRequiredData;
        const typeFraisCollection: ITypeFrais[] = [sampleWithPartialData];
        expectedResult = service.addTypeFraisToCollectionIfMissing(typeFraisCollection, typeFrais);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeFrais);
      });

      it('should add only unique TypeFrais to an array', () => {
        const typeFraisArray: ITypeFrais[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeFraisCollection: ITypeFrais[] = [sampleWithRequiredData];
        expectedResult = service.addTypeFraisToCollectionIfMissing(typeFraisCollection, ...typeFraisArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeFrais: ITypeFrais = sampleWithRequiredData;
        const typeFrais2: ITypeFrais = sampleWithPartialData;
        expectedResult = service.addTypeFraisToCollectionIfMissing([], typeFrais, typeFrais2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeFrais);
        expect(expectedResult).toContain(typeFrais2);
      });

      it('should accept null and undefined values', () => {
        const typeFrais: ITypeFrais = sampleWithRequiredData;
        expectedResult = service.addTypeFraisToCollectionIfMissing([], null, typeFrais, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeFrais);
      });

      it('should return initial array if no TypeFrais is added', () => {
        const typeFraisCollection: ITypeFrais[] = [sampleWithRequiredData];
        expectedResult = service.addTypeFraisToCollectionIfMissing(typeFraisCollection, undefined, null);
        expect(expectedResult).toEqual(typeFraisCollection);
      });
    });

    describe('compareTypeFrais', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeFrais(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeFrais(entity1, entity2);
        const compareResult2 = service.compareTypeFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeFrais(entity1, entity2);
        const compareResult2 = service.compareTypeFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeFrais(entity1, entity2);
        const compareResult2 = service.compareTypeFrais(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
