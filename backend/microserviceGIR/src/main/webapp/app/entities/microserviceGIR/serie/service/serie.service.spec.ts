import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISerie } from '../serie.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../serie.test-samples';

import { SerieService } from './serie.service';

const requireRestSample: ISerie = {
  ...sampleWithRequiredData,
};

describe('Serie Service', () => {
  let service: SerieService;
  let httpMock: HttpTestingController;
  let expectedResult: ISerie | ISerie[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SerieService);
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

    it('should create a Serie', () => {
      const serie = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Serie', () => {
      const serie = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serie).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Serie', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Serie', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Serie', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSerieToCollectionIfMissing', () => {
      it('should add a Serie to an empty array', () => {
        const serie: ISerie = sampleWithRequiredData;
        expectedResult = service.addSerieToCollectionIfMissing([], serie);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serie);
      });

      it('should not add a Serie to an array that contains it', () => {
        const serie: ISerie = sampleWithRequiredData;
        const serieCollection: ISerie[] = [
          {
            ...serie,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSerieToCollectionIfMissing(serieCollection, serie);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Serie to an array that doesn't contain it", () => {
        const serie: ISerie = sampleWithRequiredData;
        const serieCollection: ISerie[] = [sampleWithPartialData];
        expectedResult = service.addSerieToCollectionIfMissing(serieCollection, serie);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serie);
      });

      it('should add only unique Serie to an array', () => {
        const serieArray: ISerie[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serieCollection: ISerie[] = [sampleWithRequiredData];
        expectedResult = service.addSerieToCollectionIfMissing(serieCollection, ...serieArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serie: ISerie = sampleWithRequiredData;
        const serie2: ISerie = sampleWithPartialData;
        expectedResult = service.addSerieToCollectionIfMissing([], serie, serie2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serie);
        expect(expectedResult).toContain(serie2);
      });

      it('should accept null and undefined values', () => {
        const serie: ISerie = sampleWithRequiredData;
        expectedResult = service.addSerieToCollectionIfMissing([], null, serie, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serie);
      });

      it('should return initial array if no Serie is added', () => {
        const serieCollection: ISerie[] = [sampleWithRequiredData];
        expectedResult = service.addSerieToCollectionIfMissing(serieCollection, undefined, null);
        expect(expectedResult).toEqual(serieCollection);
      });
    });

    describe('compareSerie', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSerie(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSerie(entity1, entity2);
        const compareResult2 = service.compareSerie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSerie(entity1, entity2);
        const compareResult2 = service.compareSerie(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSerie(entity1, entity2);
        const compareResult2 = service.compareSerie(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
