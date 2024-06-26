import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeHandicap } from '../type-handicap.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-handicap.test-samples';

import { TypeHandicapService } from './type-handicap.service';

const requireRestSample: ITypeHandicap = {
  ...sampleWithRequiredData,
};

describe('TypeHandicap Service', () => {
  let service: TypeHandicapService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeHandicap | ITypeHandicap[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeHandicapService);
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

    it('should create a TypeHandicap', () => {
      const typeHandicap = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeHandicap).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeHandicap', () => {
      const typeHandicap = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeHandicap).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeHandicap', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeHandicap', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeHandicap', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TypeHandicap', () => {
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

    describe('addTypeHandicapToCollectionIfMissing', () => {
      it('should add a TypeHandicap to an empty array', () => {
        const typeHandicap: ITypeHandicap = sampleWithRequiredData;
        expectedResult = service.addTypeHandicapToCollectionIfMissing([], typeHandicap);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeHandicap);
      });

      it('should not add a TypeHandicap to an array that contains it', () => {
        const typeHandicap: ITypeHandicap = sampleWithRequiredData;
        const typeHandicapCollection: ITypeHandicap[] = [
          {
            ...typeHandicap,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeHandicapToCollectionIfMissing(typeHandicapCollection, typeHandicap);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeHandicap to an array that doesn't contain it", () => {
        const typeHandicap: ITypeHandicap = sampleWithRequiredData;
        const typeHandicapCollection: ITypeHandicap[] = [sampleWithPartialData];
        expectedResult = service.addTypeHandicapToCollectionIfMissing(typeHandicapCollection, typeHandicap);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeHandicap);
      });

      it('should add only unique TypeHandicap to an array', () => {
        const typeHandicapArray: ITypeHandicap[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeHandicapCollection: ITypeHandicap[] = [sampleWithRequiredData];
        expectedResult = service.addTypeHandicapToCollectionIfMissing(typeHandicapCollection, ...typeHandicapArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeHandicap: ITypeHandicap = sampleWithRequiredData;
        const typeHandicap2: ITypeHandicap = sampleWithPartialData;
        expectedResult = service.addTypeHandicapToCollectionIfMissing([], typeHandicap, typeHandicap2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeHandicap);
        expect(expectedResult).toContain(typeHandicap2);
      });

      it('should accept null and undefined values', () => {
        const typeHandicap: ITypeHandicap = sampleWithRequiredData;
        expectedResult = service.addTypeHandicapToCollectionIfMissing([], null, typeHandicap, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeHandicap);
      });

      it('should return initial array if no TypeHandicap is added', () => {
        const typeHandicapCollection: ITypeHandicap[] = [sampleWithRequiredData];
        expectedResult = service.addTypeHandicapToCollectionIfMissing(typeHandicapCollection, undefined, null);
        expect(expectedResult).toEqual(typeHandicapCollection);
      });
    });

    describe('compareTypeHandicap', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeHandicap(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeHandicap(entity1, entity2);
        const compareResult2 = service.compareTypeHandicap(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeHandicap(entity1, entity2);
        const compareResult2 = service.compareTypeHandicap(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeHandicap(entity1, entity2);
        const compareResult2 = service.compareTypeHandicap(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
