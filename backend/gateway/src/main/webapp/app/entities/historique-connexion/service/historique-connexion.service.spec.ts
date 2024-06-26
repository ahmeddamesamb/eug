import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHistoriqueConnexion } from '../historique-connexion.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../historique-connexion.test-samples';

import { HistoriqueConnexionService, RestHistoriqueConnexion } from './historique-connexion.service';

const requireRestSample: RestHistoriqueConnexion = {
  ...sampleWithRequiredData,
  dateDebutConnexion: sampleWithRequiredData.dateDebutConnexion?.format(DATE_FORMAT),
  dateFinConnexion: sampleWithRequiredData.dateFinConnexion?.format(DATE_FORMAT),
};

describe('HistoriqueConnexion Service', () => {
  let service: HistoriqueConnexionService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistoriqueConnexion | IHistoriqueConnexion[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HistoriqueConnexionService);
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

    it('should create a HistoriqueConnexion', () => {
      const historiqueConnexion = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(historiqueConnexion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HistoriqueConnexion', () => {
      const historiqueConnexion = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(historiqueConnexion).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HistoriqueConnexion', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HistoriqueConnexion', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HistoriqueConnexion', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a HistoriqueConnexion', () => {
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

    describe('addHistoriqueConnexionToCollectionIfMissing', () => {
      it('should add a HistoriqueConnexion to an empty array', () => {
        const historiqueConnexion: IHistoriqueConnexion = sampleWithRequiredData;
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing([], historiqueConnexion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueConnexion);
      });

      it('should not add a HistoriqueConnexion to an array that contains it', () => {
        const historiqueConnexion: IHistoriqueConnexion = sampleWithRequiredData;
        const historiqueConnexionCollection: IHistoriqueConnexion[] = [
          {
            ...historiqueConnexion,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing(historiqueConnexionCollection, historiqueConnexion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HistoriqueConnexion to an array that doesn't contain it", () => {
        const historiqueConnexion: IHistoriqueConnexion = sampleWithRequiredData;
        const historiqueConnexionCollection: IHistoriqueConnexion[] = [sampleWithPartialData];
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing(historiqueConnexionCollection, historiqueConnexion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueConnexion);
      });

      it('should add only unique HistoriqueConnexion to an array', () => {
        const historiqueConnexionArray: IHistoriqueConnexion[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historiqueConnexionCollection: IHistoriqueConnexion[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing(historiqueConnexionCollection, ...historiqueConnexionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historiqueConnexion: IHistoriqueConnexion = sampleWithRequiredData;
        const historiqueConnexion2: IHistoriqueConnexion = sampleWithPartialData;
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing([], historiqueConnexion, historiqueConnexion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueConnexion);
        expect(expectedResult).toContain(historiqueConnexion2);
      });

      it('should accept null and undefined values', () => {
        const historiqueConnexion: IHistoriqueConnexion = sampleWithRequiredData;
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing([], null, historiqueConnexion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueConnexion);
      });

      it('should return initial array if no HistoriqueConnexion is added', () => {
        const historiqueConnexionCollection: IHistoriqueConnexion[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueConnexionToCollectionIfMissing(historiqueConnexionCollection, undefined, null);
        expect(expectedResult).toEqual(historiqueConnexionCollection);
      });
    });

    describe('compareHistoriqueConnexion', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistoriqueConnexion(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHistoriqueConnexion(entity1, entity2);
        const compareResult2 = service.compareHistoriqueConnexion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHistoriqueConnexion(entity1, entity2);
        const compareResult2 = service.compareHistoriqueConnexion(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHistoriqueConnexion(entity1, entity2);
        const compareResult2 = service.compareHistoriqueConnexion(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
