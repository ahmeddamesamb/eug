import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../inscription-administrative-formation.test-samples';

import {
  InscriptionAdministrativeFormationService,
  RestInscriptionAdministrativeFormation,
} from './inscription-administrative-formation.service';

const requireRestSample: RestInscriptionAdministrativeFormation = {
  ...sampleWithRequiredData,
  dateChoixFormation: sampleWithRequiredData.dateChoixFormation?.toJSON(),
  dateValidationInscription: sampleWithRequiredData.dateValidationInscription?.toJSON(),
};

describe('InscriptionAdministrativeFormation Service', () => {
  let service: InscriptionAdministrativeFormationService;
  let httpMock: HttpTestingController;
  let expectedResult: IInscriptionAdministrativeFormation | IInscriptionAdministrativeFormation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InscriptionAdministrativeFormationService);
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

    it('should create a InscriptionAdministrativeFormation', () => {
      const inscriptionAdministrativeFormation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inscriptionAdministrativeFormation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InscriptionAdministrativeFormation', () => {
      const inscriptionAdministrativeFormation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inscriptionAdministrativeFormation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InscriptionAdministrativeFormation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InscriptionAdministrativeFormation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InscriptionAdministrativeFormation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InscriptionAdministrativeFormation', () => {
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

    describe('addInscriptionAdministrativeFormationToCollectionIfMissing', () => {
      it('should add a InscriptionAdministrativeFormation to an empty array', () => {
        const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = sampleWithRequiredData;
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing([], inscriptionAdministrativeFormation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionAdministrativeFormation);
      });

      it('should not add a InscriptionAdministrativeFormation to an array that contains it', () => {
        const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = sampleWithRequiredData;
        const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [
          {
            ...inscriptionAdministrativeFormation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          inscriptionAdministrativeFormationCollection,
          inscriptionAdministrativeFormation,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InscriptionAdministrativeFormation to an array that doesn't contain it", () => {
        const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = sampleWithRequiredData;
        const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [sampleWithPartialData];
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          inscriptionAdministrativeFormationCollection,
          inscriptionAdministrativeFormation,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionAdministrativeFormation);
      });

      it('should add only unique InscriptionAdministrativeFormation to an array', () => {
        const inscriptionAdministrativeFormationArray: IInscriptionAdministrativeFormation[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          inscriptionAdministrativeFormationCollection,
          ...inscriptionAdministrativeFormationArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = sampleWithRequiredData;
        const inscriptionAdministrativeFormation2: IInscriptionAdministrativeFormation = sampleWithPartialData;
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          [],
          inscriptionAdministrativeFormation,
          inscriptionAdministrativeFormation2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionAdministrativeFormation);
        expect(expectedResult).toContain(inscriptionAdministrativeFormation2);
      });

      it('should accept null and undefined values', () => {
        const inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation = sampleWithRequiredData;
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          [],
          null,
          inscriptionAdministrativeFormation,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionAdministrativeFormation);
      });

      it('should return initial array if no InscriptionAdministrativeFormation is added', () => {
        const inscriptionAdministrativeFormationCollection: IInscriptionAdministrativeFormation[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionAdministrativeFormationToCollectionIfMissing(
          inscriptionAdministrativeFormationCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(inscriptionAdministrativeFormationCollection);
      });
    });

    describe('compareInscriptionAdministrativeFormation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInscriptionAdministrativeFormation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInscriptionAdministrativeFormation(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrativeFormation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInscriptionAdministrativeFormation(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrativeFormation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInscriptionAdministrativeFormation(entity1, entity2);
        const compareResult2 = service.compareInscriptionAdministrativeFormation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
