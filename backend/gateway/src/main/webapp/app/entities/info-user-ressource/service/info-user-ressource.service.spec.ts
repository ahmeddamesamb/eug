import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInfoUserRessource } from '../info-user-ressource.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../info-user-ressource.test-samples';

import { InfoUserRessourceService, RestInfoUserRessource } from './info-user-ressource.service';

const requireRestSample: RestInfoUserRessource = {
  ...sampleWithRequiredData,
  dateAjout: sampleWithRequiredData.dateAjout?.format(DATE_FORMAT),
};

describe('InfoUserRessource Service', () => {
  let service: InfoUserRessourceService;
  let httpMock: HttpTestingController;
  let expectedResult: IInfoUserRessource | IInfoUserRessource[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfoUserRessourceService);
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

    it('should create a InfoUserRessource', () => {
      const infoUserRessource = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(infoUserRessource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfoUserRessource', () => {
      const infoUserRessource = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(infoUserRessource).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InfoUserRessource', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfoUserRessource', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InfoUserRessource', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InfoUserRessource', () => {
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

    describe('addInfoUserRessourceToCollectionIfMissing', () => {
      it('should add a InfoUserRessource to an empty array', () => {
        const infoUserRessource: IInfoUserRessource = sampleWithRequiredData;
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing([], infoUserRessource);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infoUserRessource);
      });

      it('should not add a InfoUserRessource to an array that contains it', () => {
        const infoUserRessource: IInfoUserRessource = sampleWithRequiredData;
        const infoUserRessourceCollection: IInfoUserRessource[] = [
          {
            ...infoUserRessource,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing(infoUserRessourceCollection, infoUserRessource);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfoUserRessource to an array that doesn't contain it", () => {
        const infoUserRessource: IInfoUserRessource = sampleWithRequiredData;
        const infoUserRessourceCollection: IInfoUserRessource[] = [sampleWithPartialData];
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing(infoUserRessourceCollection, infoUserRessource);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infoUserRessource);
      });

      it('should add only unique InfoUserRessource to an array', () => {
        const infoUserRessourceArray: IInfoUserRessource[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const infoUserRessourceCollection: IInfoUserRessource[] = [sampleWithRequiredData];
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing(infoUserRessourceCollection, ...infoUserRessourceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infoUserRessource: IInfoUserRessource = sampleWithRequiredData;
        const infoUserRessource2: IInfoUserRessource = sampleWithPartialData;
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing([], infoUserRessource, infoUserRessource2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infoUserRessource);
        expect(expectedResult).toContain(infoUserRessource2);
      });

      it('should accept null and undefined values', () => {
        const infoUserRessource: IInfoUserRessource = sampleWithRequiredData;
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing([], null, infoUserRessource, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infoUserRessource);
      });

      it('should return initial array if no InfoUserRessource is added', () => {
        const infoUserRessourceCollection: IInfoUserRessource[] = [sampleWithRequiredData];
        expectedResult = service.addInfoUserRessourceToCollectionIfMissing(infoUserRessourceCollection, undefined, null);
        expect(expectedResult).toEqual(infoUserRessourceCollection);
      });
    });

    describe('compareInfoUserRessource', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInfoUserRessource(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInfoUserRessource(entity1, entity2);
        const compareResult2 = service.compareInfoUserRessource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInfoUserRessource(entity1, entity2);
        const compareResult2 = service.compareInfoUserRessource(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInfoUserRessource(entity1, entity2);
        const compareResult2 = service.compareInfoUserRessource(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
