import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeAdmission } from '../type-admission.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-admission.test-samples';

import { TypeAdmissionService } from './type-admission.service';

const requireRestSample: ITypeAdmission = {
  ...sampleWithRequiredData,
};

describe('TypeAdmission Service', () => {
  let service: TypeAdmissionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeAdmission | ITypeAdmission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeAdmissionService);
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

    it('should create a TypeAdmission', () => {
      const typeAdmission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeAdmission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeAdmission', () => {
      const typeAdmission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeAdmission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeAdmission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeAdmission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeAdmission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a TypeAdmission', () => {
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

    describe('addTypeAdmissionToCollectionIfMissing', () => {
      it('should add a TypeAdmission to an empty array', () => {
        const typeAdmission: ITypeAdmission = sampleWithRequiredData;
        expectedResult = service.addTypeAdmissionToCollectionIfMissing([], typeAdmission);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeAdmission);
      });

      it('should not add a TypeAdmission to an array that contains it', () => {
        const typeAdmission: ITypeAdmission = sampleWithRequiredData;
        const typeAdmissionCollection: ITypeAdmission[] = [
          {
            ...typeAdmission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeAdmissionToCollectionIfMissing(typeAdmissionCollection, typeAdmission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeAdmission to an array that doesn't contain it", () => {
        const typeAdmission: ITypeAdmission = sampleWithRequiredData;
        const typeAdmissionCollection: ITypeAdmission[] = [sampleWithPartialData];
        expectedResult = service.addTypeAdmissionToCollectionIfMissing(typeAdmissionCollection, typeAdmission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeAdmission);
      });

      it('should add only unique TypeAdmission to an array', () => {
        const typeAdmissionArray: ITypeAdmission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeAdmissionCollection: ITypeAdmission[] = [sampleWithRequiredData];
        expectedResult = service.addTypeAdmissionToCollectionIfMissing(typeAdmissionCollection, ...typeAdmissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeAdmission: ITypeAdmission = sampleWithRequiredData;
        const typeAdmission2: ITypeAdmission = sampleWithPartialData;
        expectedResult = service.addTypeAdmissionToCollectionIfMissing([], typeAdmission, typeAdmission2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeAdmission);
        expect(expectedResult).toContain(typeAdmission2);
      });

      it('should accept null and undefined values', () => {
        const typeAdmission: ITypeAdmission = sampleWithRequiredData;
        expectedResult = service.addTypeAdmissionToCollectionIfMissing([], null, typeAdmission, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeAdmission);
      });

      it('should return initial array if no TypeAdmission is added', () => {
        const typeAdmissionCollection: ITypeAdmission[] = [sampleWithRequiredData];
        expectedResult = service.addTypeAdmissionToCollectionIfMissing(typeAdmissionCollection, undefined, null);
        expect(expectedResult).toEqual(typeAdmissionCollection);
      });
    });

    describe('compareTypeAdmission', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeAdmission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeAdmission(entity1, entity2);
        const compareResult2 = service.compareTypeAdmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeAdmission(entity1, entity2);
        const compareResult2 = service.compareTypeAdmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeAdmission(entity1, entity2);
        const compareResult2 = service.compareTypeAdmission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
