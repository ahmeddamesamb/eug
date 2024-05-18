import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../paiement-formation-privee.test-samples';

import { PaiementFormationPriveeService, RestPaiementFormationPrivee } from './paiement-formation-privee.service';

const requireRestSample: RestPaiementFormationPrivee = {
  ...sampleWithRequiredData,
  datePaiement: sampleWithRequiredData.datePaiement?.toJSON(),
};

describe('PaiementFormationPrivee Service', () => {
  let service: PaiementFormationPriveeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaiementFormationPrivee | IPaiementFormationPrivee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaiementFormationPriveeService);
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

    it('should create a PaiementFormationPrivee', () => {
      const paiementFormationPrivee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paiementFormationPrivee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaiementFormationPrivee', () => {
      const paiementFormationPrivee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paiementFormationPrivee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaiementFormationPrivee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaiementFormationPrivee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaiementFormationPrivee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaiementFormationPriveeToCollectionIfMissing', () => {
      it('should add a PaiementFormationPrivee to an empty array', () => {
        const paiementFormationPrivee: IPaiementFormationPrivee = sampleWithRequiredData;
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing([], paiementFormationPrivee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementFormationPrivee);
      });

      it('should not add a PaiementFormationPrivee to an array that contains it', () => {
        const paiementFormationPrivee: IPaiementFormationPrivee = sampleWithRequiredData;
        const paiementFormationPriveeCollection: IPaiementFormationPrivee[] = [
          {
            ...paiementFormationPrivee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing(
          paiementFormationPriveeCollection,
          paiementFormationPrivee,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaiementFormationPrivee to an array that doesn't contain it", () => {
        const paiementFormationPrivee: IPaiementFormationPrivee = sampleWithRequiredData;
        const paiementFormationPriveeCollection: IPaiementFormationPrivee[] = [sampleWithPartialData];
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing(
          paiementFormationPriveeCollection,
          paiementFormationPrivee,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementFormationPrivee);
      });

      it('should add only unique PaiementFormationPrivee to an array', () => {
        const paiementFormationPriveeArray: IPaiementFormationPrivee[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const paiementFormationPriveeCollection: IPaiementFormationPrivee[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing(
          paiementFormationPriveeCollection,
          ...paiementFormationPriveeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paiementFormationPrivee: IPaiementFormationPrivee = sampleWithRequiredData;
        const paiementFormationPrivee2: IPaiementFormationPrivee = sampleWithPartialData;
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing([], paiementFormationPrivee, paiementFormationPrivee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementFormationPrivee);
        expect(expectedResult).toContain(paiementFormationPrivee2);
      });

      it('should accept null and undefined values', () => {
        const paiementFormationPrivee: IPaiementFormationPrivee = sampleWithRequiredData;
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing([], null, paiementFormationPrivee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementFormationPrivee);
      });

      it('should return initial array if no PaiementFormationPrivee is added', () => {
        const paiementFormationPriveeCollection: IPaiementFormationPrivee[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementFormationPriveeToCollectionIfMissing(paiementFormationPriveeCollection, undefined, null);
        expect(expectedResult).toEqual(paiementFormationPriveeCollection);
      });
    });

    describe('comparePaiementFormationPrivee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaiementFormationPrivee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaiementFormationPrivee(entity1, entity2);
        const compareResult2 = service.comparePaiementFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaiementFormationPrivee(entity1, entity2);
        const compareResult2 = service.comparePaiementFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaiementFormationPrivee(entity1, entity2);
        const compareResult2 = service.comparePaiementFormationPrivee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
