import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeFormation } from '../type-formation.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-formation.test-samples';

import { TypeFormationService } from './type-formation.service';

const requireRestSample: ITypeFormation = {
  ...sampleWithRequiredData,
};

describe('TypeFormation Service', () => {
  let service: TypeFormationService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeFormation | ITypeFormation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeFormationService);
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

    it('should create a TypeFormation', () => {
      const typeFormation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeFormation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeFormation', () => {
      const typeFormation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeFormation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeFormation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeFormation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeFormation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TypeFormation', () => {
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

    describe('addTypeFormationToCollectionIfMissing', () => {
      it('should add a TypeFormation to an empty array', () => {
        const typeFormation: ITypeFormation = sampleWithRequiredData;
        expectedResult = service.addTypeFormationToCollectionIfMissing([], typeFormation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeFormation);
      });

      it('should not add a TypeFormation to an array that contains it', () => {
        const typeFormation: ITypeFormation = sampleWithRequiredData;
        const typeFormationCollection: ITypeFormation[] = [
          {
            ...typeFormation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeFormationToCollectionIfMissing(typeFormationCollection, typeFormation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeFormation to an array that doesn't contain it", () => {
        const typeFormation: ITypeFormation = sampleWithRequiredData;
        const typeFormationCollection: ITypeFormation[] = [sampleWithPartialData];
        expectedResult = service.addTypeFormationToCollectionIfMissing(typeFormationCollection, typeFormation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeFormation);
      });

      it('should add only unique TypeFormation to an array', () => {
        const typeFormationArray: ITypeFormation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeFormationCollection: ITypeFormation[] = [sampleWithRequiredData];
        expectedResult = service.addTypeFormationToCollectionIfMissing(typeFormationCollection, ...typeFormationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeFormation: ITypeFormation = sampleWithRequiredData;
        const typeFormation2: ITypeFormation = sampleWithPartialData;
        expectedResult = service.addTypeFormationToCollectionIfMissing([], typeFormation, typeFormation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeFormation);
        expect(expectedResult).toContain(typeFormation2);
      });

      it('should accept null and undefined values', () => {
        const typeFormation: ITypeFormation = sampleWithRequiredData;
        expectedResult = service.addTypeFormationToCollectionIfMissing([], null, typeFormation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeFormation);
      });

      it('should return initial array if no TypeFormation is added', () => {
        const typeFormationCollection: ITypeFormation[] = [sampleWithRequiredData];
        expectedResult = service.addTypeFormationToCollectionIfMissing(typeFormationCollection, undefined, null);
        expect(expectedResult).toEqual(typeFormationCollection);
      });
    });

    describe('compareTypeFormation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeFormation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeFormation(entity1, entity2);
        const compareResult2 = service.compareTypeFormation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeFormation(entity1, entity2);
        const compareResult2 = service.compareTypeFormation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeFormation(entity1, entity2);
        const compareResult2 = service.compareTypeFormation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
