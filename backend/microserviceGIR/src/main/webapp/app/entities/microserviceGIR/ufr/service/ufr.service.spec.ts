import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUfr } from '../ufr.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ufr.test-samples';

import { UfrService } from './ufr.service';

const requireRestSample: IUfr = {
  ...sampleWithRequiredData,
};

describe('Ufr Service', () => {
  let service: UfrService;
  let httpMock: HttpTestingController;
  let expectedResult: IUfr | IUfr[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UfrService);
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

    it('should create a Ufr', () => {
      const ufr = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ufr).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ufr', () => {
      const ufr = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ufr).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ufr', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ufr', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ufr', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Ufr', () => {
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

    describe('addUfrToCollectionIfMissing', () => {
      it('should add a Ufr to an empty array', () => {
        const ufr: IUfr = sampleWithRequiredData;
        expectedResult = service.addUfrToCollectionIfMissing([], ufr);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ufr);
      });

      it('should not add a Ufr to an array that contains it', () => {
        const ufr: IUfr = sampleWithRequiredData;
        const ufrCollection: IUfr[] = [
          {
            ...ufr,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUfrToCollectionIfMissing(ufrCollection, ufr);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ufr to an array that doesn't contain it", () => {
        const ufr: IUfr = sampleWithRequiredData;
        const ufrCollection: IUfr[] = [sampleWithPartialData];
        expectedResult = service.addUfrToCollectionIfMissing(ufrCollection, ufr);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ufr);
      });

      it('should add only unique Ufr to an array', () => {
        const ufrArray: IUfr[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ufrCollection: IUfr[] = [sampleWithRequiredData];
        expectedResult = service.addUfrToCollectionIfMissing(ufrCollection, ...ufrArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ufr: IUfr = sampleWithRequiredData;
        const ufr2: IUfr = sampleWithPartialData;
        expectedResult = service.addUfrToCollectionIfMissing([], ufr, ufr2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ufr);
        expect(expectedResult).toContain(ufr2);
      });

      it('should accept null and undefined values', () => {
        const ufr: IUfr = sampleWithRequiredData;
        expectedResult = service.addUfrToCollectionIfMissing([], null, ufr, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ufr);
      });

      it('should return initial array if no Ufr is added', () => {
        const ufrCollection: IUfr[] = [sampleWithRequiredData];
        expectedResult = service.addUfrToCollectionIfMissing(ufrCollection, undefined, null);
        expect(expectedResult).toEqual(ufrCollection);
      });
    });

    describe('compareUfr', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUfr(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUfr(entity1, entity2);
        const compareResult2 = service.compareUfr(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUfr(entity1, entity2);
        const compareResult2 = service.compareUfr(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUfr(entity1, entity2);
        const compareResult2 = service.compareUfr(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
