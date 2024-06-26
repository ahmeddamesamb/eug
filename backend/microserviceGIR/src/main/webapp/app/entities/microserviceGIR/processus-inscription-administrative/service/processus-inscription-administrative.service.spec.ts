import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../processus-inscription-administrative.test-samples';

import {
  ProcessusInscriptionAdministrativeService,
  RestProcessusInscriptionAdministrative,
} from './processus-inscription-administrative.service';

const requireRestSample: RestProcessusInscriptionAdministrative = {
  ...sampleWithRequiredData,
  dateDemarrageInscription: sampleWithRequiredData.dateDemarrageInscription?.toJSON(),
  dateAnnulationInscription: sampleWithRequiredData.dateAnnulationInscription?.toJSON(),
  dateVisiteMedicale: sampleWithRequiredData.dateVisiteMedicale?.toJSON(),
  dateRemiseQuitusCROUS: sampleWithRequiredData.dateRemiseQuitusCROUS?.toJSON(),
  dateRemiseQuitusBU: sampleWithRequiredData.dateRemiseQuitusBU?.toJSON(),
  datePaiementFraisObligatoires: sampleWithRequiredData.datePaiementFraisObligatoires?.toJSON(),
  dateRemiseCarteEtu: sampleWithRequiredData.dateRemiseCarteEtu?.toJSON(),
  dateInscriptionAdministrative: sampleWithRequiredData.dateInscriptionAdministrative?.toJSON(),
  derniereModification: sampleWithRequiredData.derniereModification?.toJSON(),
};

describe('ProcessusInscriptionAdministrative Service', () => {
  let service: ProcessusInscriptionAdministrativeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcessusInscriptionAdministrative | IProcessusInscriptionAdministrative[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProcessusInscriptionAdministrativeService);
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

    it('should create a ProcessusInscriptionAdministrative', () => {
      const processusInscriptionAdministrative = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(processusInscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProcessusInscriptionAdministrative', () => {
      const processusInscriptionAdministrative = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(processusInscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProcessusInscriptionAdministrative', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProcessusInscriptionAdministrative', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProcessusInscriptionAdministrative', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ProcessusInscriptionAdministrative', () => {
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

    describe('addProcessusInscriptionAdministrativeToCollectionIfMissing', () => {
      it('should add a ProcessusInscriptionAdministrative to an empty array', () => {
        const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing([], processusInscriptionAdministrative);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processusInscriptionAdministrative);
      });

      it('should not add a ProcessusInscriptionAdministrative to an array that contains it', () => {
        const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = sampleWithRequiredData;
        const processusInscriptionAdministrativeCollection: IProcessusInscriptionAdministrative[] = [
          {
            ...processusInscriptionAdministrative,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          processusInscriptionAdministrativeCollection,
          processusInscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProcessusInscriptionAdministrative to an array that doesn't contain it", () => {
        const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = sampleWithRequiredData;
        const processusInscriptionAdministrativeCollection: IProcessusInscriptionAdministrative[] = [sampleWithPartialData];
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          processusInscriptionAdministrativeCollection,
          processusInscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processusInscriptionAdministrative);
      });

      it('should add only unique ProcessusInscriptionAdministrative to an array', () => {
        const processusInscriptionAdministrativeArray: IProcessusInscriptionAdministrative[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const processusInscriptionAdministrativeCollection: IProcessusInscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          processusInscriptionAdministrativeCollection,
          ...processusInscriptionAdministrativeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = sampleWithRequiredData;
        const processusInscriptionAdministrative2: IProcessusInscriptionAdministrative = sampleWithPartialData;
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          [],
          processusInscriptionAdministrative,
          processusInscriptionAdministrative2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processusInscriptionAdministrative);
        expect(expectedResult).toContain(processusInscriptionAdministrative2);
      });

      it('should accept null and undefined values', () => {
        const processusInscriptionAdministrative: IProcessusInscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          [],
          null,
          processusInscriptionAdministrative,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processusInscriptionAdministrative);
      });

      it('should return initial array if no ProcessusInscriptionAdministrative is added', () => {
        const processusInscriptionAdministrativeCollection: IProcessusInscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addProcessusInscriptionAdministrativeToCollectionIfMissing(
          processusInscriptionAdministrativeCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(processusInscriptionAdministrativeCollection);
      });
    });

    describe('compareProcessusInscriptionAdministrative', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcessusInscriptionAdministrative(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcessusInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcessusInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcessusInscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusInscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
