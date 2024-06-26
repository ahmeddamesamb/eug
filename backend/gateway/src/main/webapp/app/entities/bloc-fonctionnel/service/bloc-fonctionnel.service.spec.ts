import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBlocFonctionnel } from '../bloc-fonctionnel.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../bloc-fonctionnel.test-samples';

import { BlocFonctionnelService, RestBlocFonctionnel } from './bloc-fonctionnel.service';

const requireRestSample: RestBlocFonctionnel = {
  ...sampleWithRequiredData,
  dateAjoutBloc: sampleWithRequiredData.dateAjoutBloc?.format(DATE_FORMAT),
};

describe('BlocFonctionnel Service', () => {
  let service: BlocFonctionnelService;
  let httpMock: HttpTestingController;
  let expectedResult: IBlocFonctionnel | IBlocFonctionnel[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BlocFonctionnelService);
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

    it('should create a BlocFonctionnel', () => {
      const blocFonctionnel = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(blocFonctionnel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BlocFonctionnel', () => {
      const blocFonctionnel = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(blocFonctionnel).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BlocFonctionnel', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BlocFonctionnel', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BlocFonctionnel', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a BlocFonctionnel', () => {
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

    describe('addBlocFonctionnelToCollectionIfMissing', () => {
      it('should add a BlocFonctionnel to an empty array', () => {
        const blocFonctionnel: IBlocFonctionnel = sampleWithRequiredData;
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing([], blocFonctionnel);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blocFonctionnel);
      });

      it('should not add a BlocFonctionnel to an array that contains it', () => {
        const blocFonctionnel: IBlocFonctionnel = sampleWithRequiredData;
        const blocFonctionnelCollection: IBlocFonctionnel[] = [
          {
            ...blocFonctionnel,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing(blocFonctionnelCollection, blocFonctionnel);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BlocFonctionnel to an array that doesn't contain it", () => {
        const blocFonctionnel: IBlocFonctionnel = sampleWithRequiredData;
        const blocFonctionnelCollection: IBlocFonctionnel[] = [sampleWithPartialData];
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing(blocFonctionnelCollection, blocFonctionnel);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blocFonctionnel);
      });

      it('should add only unique BlocFonctionnel to an array', () => {
        const blocFonctionnelArray: IBlocFonctionnel[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const blocFonctionnelCollection: IBlocFonctionnel[] = [sampleWithRequiredData];
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing(blocFonctionnelCollection, ...blocFonctionnelArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const blocFonctionnel: IBlocFonctionnel = sampleWithRequiredData;
        const blocFonctionnel2: IBlocFonctionnel = sampleWithPartialData;
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing([], blocFonctionnel, blocFonctionnel2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(blocFonctionnel);
        expect(expectedResult).toContain(blocFonctionnel2);
      });

      it('should accept null and undefined values', () => {
        const blocFonctionnel: IBlocFonctionnel = sampleWithRequiredData;
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing([], null, blocFonctionnel, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(blocFonctionnel);
      });

      it('should return initial array if no BlocFonctionnel is added', () => {
        const blocFonctionnelCollection: IBlocFonctionnel[] = [sampleWithRequiredData];
        expectedResult = service.addBlocFonctionnelToCollectionIfMissing(blocFonctionnelCollection, undefined, null);
        expect(expectedResult).toEqual(blocFonctionnelCollection);
      });
    });

    describe('compareBlocFonctionnel', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBlocFonctionnel(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareBlocFonctionnel(entity1, entity2);
        const compareResult2 = service.compareBlocFonctionnel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareBlocFonctionnel(entity1, entity2);
        const compareResult2 = service.compareBlocFonctionnel(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareBlocFonctionnel(entity1, entity2);
        const compareResult2 = service.compareBlocFonctionnel(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
