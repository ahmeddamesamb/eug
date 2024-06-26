import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILycee } from '../lycee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../lycee.test-samples';

import { LyceeService } from './lycee.service';

const requireRestSample: ILycee = {
  ...sampleWithRequiredData,
};

describe('Lycee Service', () => {
  let service: LyceeService;
  let httpMock: HttpTestingController;
  let expectedResult: ILycee | ILycee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(LyceeService);
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

    it('should create a Lycee', () => {
      const lycee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(lycee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Lycee', () => {
      const lycee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(lycee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Lycee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Lycee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Lycee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Lycee', () => {
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

    describe('addLyceeToCollectionIfMissing', () => {
      it('should add a Lycee to an empty array', () => {
        const lycee: ILycee = sampleWithRequiredData;
        expectedResult = service.addLyceeToCollectionIfMissing([], lycee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lycee);
      });

      it('should not add a Lycee to an array that contains it', () => {
        const lycee: ILycee = sampleWithRequiredData;
        const lyceeCollection: ILycee[] = [
          {
            ...lycee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addLyceeToCollectionIfMissing(lyceeCollection, lycee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Lycee to an array that doesn't contain it", () => {
        const lycee: ILycee = sampleWithRequiredData;
        const lyceeCollection: ILycee[] = [sampleWithPartialData];
        expectedResult = service.addLyceeToCollectionIfMissing(lyceeCollection, lycee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lycee);
      });

      it('should add only unique Lycee to an array', () => {
        const lyceeArray: ILycee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const lyceeCollection: ILycee[] = [sampleWithRequiredData];
        expectedResult = service.addLyceeToCollectionIfMissing(lyceeCollection, ...lyceeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const lycee: ILycee = sampleWithRequiredData;
        const lycee2: ILycee = sampleWithPartialData;
        expectedResult = service.addLyceeToCollectionIfMissing([], lycee, lycee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(lycee);
        expect(expectedResult).toContain(lycee2);
      });

      it('should accept null and undefined values', () => {
        const lycee: ILycee = sampleWithRequiredData;
        expectedResult = service.addLyceeToCollectionIfMissing([], null, lycee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(lycee);
      });

      it('should return initial array if no Lycee is added', () => {
        const lyceeCollection: ILycee[] = [sampleWithRequiredData];
        expectedResult = service.addLyceeToCollectionIfMissing(lyceeCollection, undefined, null);
        expect(expectedResult).toEqual(lyceeCollection);
      });
    });

    describe('compareLycee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareLycee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareLycee(entity1, entity2);
        const compareResult2 = service.compareLycee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareLycee(entity1, entity2);
        const compareResult2 = service.compareLycee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareLycee(entity1, entity2);
        const compareResult2 = service.compareLycee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
