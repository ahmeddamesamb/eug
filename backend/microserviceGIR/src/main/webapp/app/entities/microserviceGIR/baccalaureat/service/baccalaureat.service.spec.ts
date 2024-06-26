import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBaccalaureat } from '../baccalaureat.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../baccalaureat.test-samples';

import { BaccalaureatService, RestBaccalaureat } from './baccalaureat.service';

const requireRestSample: RestBaccalaureat = {
  ...sampleWithRequiredData,
  anneeBac: sampleWithRequiredData.anneeBac?.format(DATE_FORMAT),
};

describe('Baccalaureat Service', () => {
  let service: BaccalaureatService;
  let httpMock: HttpTestingController;
  let expectedResult: IBaccalaureat | IBaccalaureat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BaccalaureatService);
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

    it('should create a Baccalaureat', () => {
      const baccalaureat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(baccalaureat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Baccalaureat', () => {
      const baccalaureat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(baccalaureat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Baccalaureat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Baccalaureat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Baccalaureat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Baccalaureat', () => {
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

    describe('addBaccalaureatToCollectionIfMissing', () => {
      it('should add a Baccalaureat to an empty array', () => {
        const baccalaureat: IBaccalaureat = sampleWithRequiredData;
        expectedResult = service.addBaccalaureatToCollectionIfMissing([], baccalaureat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baccalaureat);
      });

      it('should not add a Baccalaureat to an array that contains it', () => {
        const baccalaureat: IBaccalaureat = sampleWithRequiredData;
        const baccalaureatCollection: IBaccalaureat[] = [
          {
            ...baccalaureat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBaccalaureatToCollectionIfMissing(baccalaureatCollection, baccalaureat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Baccalaureat to an array that doesn't contain it", () => {
        const baccalaureat: IBaccalaureat = sampleWithRequiredData;
        const baccalaureatCollection: IBaccalaureat[] = [sampleWithPartialData];
        expectedResult = service.addBaccalaureatToCollectionIfMissing(baccalaureatCollection, baccalaureat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baccalaureat);
      });

      it('should add only unique Baccalaureat to an array', () => {
        const baccalaureatArray: IBaccalaureat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const baccalaureatCollection: IBaccalaureat[] = [sampleWithRequiredData];
        expectedResult = service.addBaccalaureatToCollectionIfMissing(baccalaureatCollection, ...baccalaureatArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const baccalaureat: IBaccalaureat = sampleWithRequiredData;
        const baccalaureat2: IBaccalaureat = sampleWithPartialData;
        expectedResult = service.addBaccalaureatToCollectionIfMissing([], baccalaureat, baccalaureat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baccalaureat);
        expect(expectedResult).toContain(baccalaureat2);
      });

      it('should accept null and undefined values', () => {
        const baccalaureat: IBaccalaureat = sampleWithRequiredData;
        expectedResult = service.addBaccalaureatToCollectionIfMissing([], null, baccalaureat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baccalaureat);
      });

      it('should return initial array if no Baccalaureat is added', () => {
        const baccalaureatCollection: IBaccalaureat[] = [sampleWithRequiredData];
        expectedResult = service.addBaccalaureatToCollectionIfMissing(baccalaureatCollection, undefined, null);
        expect(expectedResult).toEqual(baccalaureatCollection);
      });
    });

    describe('compareBaccalaureat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBaccalaureat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBaccalaureat(entity1, entity2);
        const compareResult2 = service.compareBaccalaureat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBaccalaureat(entity1, entity2);
        const compareResult2 = service.compareBaccalaureat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBaccalaureat(entity1, entity2);
        const compareResult2 = service.compareBaccalaureat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
