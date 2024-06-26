import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeRapport } from '../type-rapport.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-rapport.test-samples';

import { TypeRapportService } from './type-rapport.service';

const requireRestSample: ITypeRapport = {
  ...sampleWithRequiredData,
};

describe('TypeRapport Service', () => {
  let service: TypeRapportService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeRapport | ITypeRapport[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeRapportService);
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

    it('should create a TypeRapport', () => {
      const typeRapport = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeRapport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeRapport', () => {
      const typeRapport = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeRapport).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeRapport', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeRapport', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeRapport', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TypeRapport', () => {
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

    describe('addTypeRapportToCollectionIfMissing', () => {
      it('should add a TypeRapport to an empty array', () => {
        const typeRapport: ITypeRapport = sampleWithRequiredData;
        expectedResult = service.addTypeRapportToCollectionIfMissing([], typeRapport);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeRapport);
      });

      it('should not add a TypeRapport to an array that contains it', () => {
        const typeRapport: ITypeRapport = sampleWithRequiredData;
        const typeRapportCollection: ITypeRapport[] = [
          {
            ...typeRapport,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeRapportToCollectionIfMissing(typeRapportCollection, typeRapport);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeRapport to an array that doesn't contain it", () => {
        const typeRapport: ITypeRapport = sampleWithRequiredData;
        const typeRapportCollection: ITypeRapport[] = [sampleWithPartialData];
        expectedResult = service.addTypeRapportToCollectionIfMissing(typeRapportCollection, typeRapport);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeRapport);
      });

      it('should add only unique TypeRapport to an array', () => {
        const typeRapportArray: ITypeRapport[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeRapportCollection: ITypeRapport[] = [sampleWithRequiredData];
        expectedResult = service.addTypeRapportToCollectionIfMissing(typeRapportCollection, ...typeRapportArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeRapport: ITypeRapport = sampleWithRequiredData;
        const typeRapport2: ITypeRapport = sampleWithPartialData;
        expectedResult = service.addTypeRapportToCollectionIfMissing([], typeRapport, typeRapport2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeRapport);
        expect(expectedResult).toContain(typeRapport2);
      });

      it('should accept null and undefined values', () => {
        const typeRapport: ITypeRapport = sampleWithRequiredData;
        expectedResult = service.addTypeRapportToCollectionIfMissing([], null, typeRapport, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeRapport);
      });

      it('should return initial array if no TypeRapport is added', () => {
        const typeRapportCollection: ITypeRapport[] = [sampleWithRequiredData];
        expectedResult = service.addTypeRapportToCollectionIfMissing(typeRapportCollection, undefined, null);
        expect(expectedResult).toEqual(typeRapportCollection);
      });
    });

    describe('compareTypeRapport', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeRapport(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeRapport(entity1, entity2);
        const compareResult2 = service.compareTypeRapport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeRapport(entity1, entity2);
        const compareResult2 = service.compareTypeRapport(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeRapport(entity1, entity2);
        const compareResult2 = service.compareTypeRapport(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
