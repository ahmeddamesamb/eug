import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProcessusDinscriptionAdministrative } from '../processus-dinscription-administrative.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../processus-dinscription-administrative.test-samples';

import {
  ProcessusDinscriptionAdministrativeService,
  RestProcessusDinscriptionAdministrative,
} from './processus-dinscription-administrative.service';

const requireRestSample: RestProcessusDinscriptionAdministrative = {
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

describe('ProcessusDinscriptionAdministrative Service', () => {
  let service: ProcessusDinscriptionAdministrativeService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcessusDinscriptionAdministrative | IProcessusDinscriptionAdministrative[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProcessusDinscriptionAdministrativeService);
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

    it('should create a ProcessusDinscriptionAdministrative', () => {
      const processusDinscriptionAdministrative = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(processusDinscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProcessusDinscriptionAdministrative', () => {
      const processusDinscriptionAdministrative = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(processusDinscriptionAdministrative).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProcessusDinscriptionAdministrative', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProcessusDinscriptionAdministrative', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProcessusDinscriptionAdministrative', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProcessusDinscriptionAdministrativeToCollectionIfMissing', () => {
      it('should add a ProcessusDinscriptionAdministrative to an empty array', () => {
        const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing([], processusDinscriptionAdministrative);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processusDinscriptionAdministrative);
      });

      it('should not add a ProcessusDinscriptionAdministrative to an array that contains it', () => {
        const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = sampleWithRequiredData;
        const processusDinscriptionAdministrativeCollection: IProcessusDinscriptionAdministrative[] = [
          {
            ...processusDinscriptionAdministrative,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          processusDinscriptionAdministrativeCollection,
          processusDinscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProcessusDinscriptionAdministrative to an array that doesn't contain it", () => {
        const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = sampleWithRequiredData;
        const processusDinscriptionAdministrativeCollection: IProcessusDinscriptionAdministrative[] = [sampleWithPartialData];
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          processusDinscriptionAdministrativeCollection,
          processusDinscriptionAdministrative,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processusDinscriptionAdministrative);
      });

      it('should add only unique ProcessusDinscriptionAdministrative to an array', () => {
        const processusDinscriptionAdministrativeArray: IProcessusDinscriptionAdministrative[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const processusDinscriptionAdministrativeCollection: IProcessusDinscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          processusDinscriptionAdministrativeCollection,
          ...processusDinscriptionAdministrativeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = sampleWithRequiredData;
        const processusDinscriptionAdministrative2: IProcessusDinscriptionAdministrative = sampleWithPartialData;
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          [],
          processusDinscriptionAdministrative,
          processusDinscriptionAdministrative2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(processusDinscriptionAdministrative);
        expect(expectedResult).toContain(processusDinscriptionAdministrative2);
      });

      it('should accept null and undefined values', () => {
        const processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative = sampleWithRequiredData;
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          [],
          null,
          processusDinscriptionAdministrative,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(processusDinscriptionAdministrative);
      });

      it('should return initial array if no ProcessusDinscriptionAdministrative is added', () => {
        const processusDinscriptionAdministrativeCollection: IProcessusDinscriptionAdministrative[] = [sampleWithRequiredData];
        expectedResult = service.addProcessusDinscriptionAdministrativeToCollectionIfMissing(
          processusDinscriptionAdministrativeCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(processusDinscriptionAdministrativeCollection);
      });
    });

    describe('compareProcessusDinscriptionAdministrative', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcessusDinscriptionAdministrative(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcessusDinscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusDinscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcessusDinscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusDinscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcessusDinscriptionAdministrative(entity1, entity2);
        const compareResult2 = service.compareProcessusDinscriptionAdministrative(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
