import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProgrammationInscription } from '../programmation-inscription.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../programmation-inscription.test-samples';

import { ProgrammationInscriptionService, RestProgrammationInscription } from './programmation-inscription.service';

const requireRestSample: RestProgrammationInscription = {
  ...sampleWithRequiredData,
  dateDebutProgrammation: sampleWithRequiredData.dateDebutProgrammation?.format(DATE_FORMAT),
  dateFinProgrammation: sampleWithRequiredData.dateFinProgrammation?.format(DATE_FORMAT),
  dateForclosClasse: sampleWithRequiredData.dateForclosClasse?.format(DATE_FORMAT),
};

describe('ProgrammationInscription Service', () => {
  let service: ProgrammationInscriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IProgrammationInscription | IProgrammationInscription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProgrammationInscriptionService);
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

    it('should create a ProgrammationInscription', () => {
      const programmationInscription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(programmationInscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProgrammationInscription', () => {
      const programmationInscription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(programmationInscription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProgrammationInscription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProgrammationInscription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProgrammationInscription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ProgrammationInscription', () => {
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

    describe('addProgrammationInscriptionToCollectionIfMissing', () => {
      it('should add a ProgrammationInscription to an empty array', () => {
        const programmationInscription: IProgrammationInscription = sampleWithRequiredData;
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing([], programmationInscription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programmationInscription);
      });

      it('should not add a ProgrammationInscription to an array that contains it', () => {
        const programmationInscription: IProgrammationInscription = sampleWithRequiredData;
        const programmationInscriptionCollection: IProgrammationInscription[] = [
          {
            ...programmationInscription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing(
          programmationInscriptionCollection,
          programmationInscription,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProgrammationInscription to an array that doesn't contain it", () => {
        const programmationInscription: IProgrammationInscription = sampleWithRequiredData;
        const programmationInscriptionCollection: IProgrammationInscription[] = [sampleWithPartialData];
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing(
          programmationInscriptionCollection,
          programmationInscription,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programmationInscription);
      });

      it('should add only unique ProgrammationInscription to an array', () => {
        const programmationInscriptionArray: IProgrammationInscription[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const programmationInscriptionCollection: IProgrammationInscription[] = [sampleWithRequiredData];
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing(
          programmationInscriptionCollection,
          ...programmationInscriptionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const programmationInscription: IProgrammationInscription = sampleWithRequiredData;
        const programmationInscription2: IProgrammationInscription = sampleWithPartialData;
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing([], programmationInscription, programmationInscription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(programmationInscription);
        expect(expectedResult).toContain(programmationInscription2);
      });

      it('should accept null and undefined values', () => {
        const programmationInscription: IProgrammationInscription = sampleWithRequiredData;
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing([], null, programmationInscription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(programmationInscription);
      });

      it('should return initial array if no ProgrammationInscription is added', () => {
        const programmationInscriptionCollection: IProgrammationInscription[] = [sampleWithRequiredData];
        expectedResult = service.addProgrammationInscriptionToCollectionIfMissing(programmationInscriptionCollection, undefined, null);
        expect(expectedResult).toEqual(programmationInscriptionCollection);
      });
    });

    describe('compareProgrammationInscription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProgrammationInscription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProgrammationInscription(entity1, entity2);
        const compareResult2 = service.compareProgrammationInscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProgrammationInscription(entity1, entity2);
        const compareResult2 = service.compareProgrammationInscription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProgrammationInscription(entity1, entity2);
        const compareResult2 = service.compareProgrammationInscription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
