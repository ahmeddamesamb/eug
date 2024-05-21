import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IPaiementFrais } from '../paiement-frais.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../paiement-frais.test-samples';

import { PaiementFraisService, RestPaiementFrais } from './paiement-frais.service';

const requireRestSample: RestPaiementFrais = {
  ...sampleWithRequiredData,
  datePaiement: sampleWithRequiredData.datePaiement?.format(DATE_FORMAT),
};

describe('PaiementFrais Service', () => {
  let service: PaiementFraisService;
  let httpMock: HttpTestingController;
  let expectedResult: IPaiementFrais | IPaiementFrais[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PaiementFraisService);
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

    it('should create a PaiementFrais', () => {
      const paiementFrais = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(paiementFrais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PaiementFrais', () => {
      const paiementFrais = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(paiementFrais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PaiementFrais', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PaiementFrais', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PaiementFrais', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaiementFraisToCollectionIfMissing', () => {
      it('should add a PaiementFrais to an empty array', () => {
        const paiementFrais: IPaiementFrais = sampleWithRequiredData;
        expectedResult = service.addPaiementFraisToCollectionIfMissing([], paiementFrais);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementFrais);
      });

      it('should not add a PaiementFrais to an array that contains it', () => {
        const paiementFrais: IPaiementFrais = sampleWithRequiredData;
        const paiementFraisCollection: IPaiementFrais[] = [
          {
            ...paiementFrais,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaiementFraisToCollectionIfMissing(paiementFraisCollection, paiementFrais);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PaiementFrais to an array that doesn't contain it", () => {
        const paiementFrais: IPaiementFrais = sampleWithRequiredData;
        const paiementFraisCollection: IPaiementFrais[] = [sampleWithPartialData];
        expectedResult = service.addPaiementFraisToCollectionIfMissing(paiementFraisCollection, paiementFrais);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementFrais);
      });

      it('should add only unique PaiementFrais to an array', () => {
        const paiementFraisArray: IPaiementFrais[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paiementFraisCollection: IPaiementFrais[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementFraisToCollectionIfMissing(paiementFraisCollection, ...paiementFraisArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const paiementFrais: IPaiementFrais = sampleWithRequiredData;
        const paiementFrais2: IPaiementFrais = sampleWithPartialData;
        expectedResult = service.addPaiementFraisToCollectionIfMissing([], paiementFrais, paiementFrais2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(paiementFrais);
        expect(expectedResult).toContain(paiementFrais2);
      });

      it('should accept null and undefined values', () => {
        const paiementFrais: IPaiementFrais = sampleWithRequiredData;
        expectedResult = service.addPaiementFraisToCollectionIfMissing([], null, paiementFrais, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(paiementFrais);
      });

      it('should return initial array if no PaiementFrais is added', () => {
        const paiementFraisCollection: IPaiementFrais[] = [sampleWithRequiredData];
        expectedResult = service.addPaiementFraisToCollectionIfMissing(paiementFraisCollection, undefined, null);
        expect(expectedResult).toEqual(paiementFraisCollection);
      });
    });

    describe('comparePaiementFrais', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePaiementFrais(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePaiementFrais(entity1, entity2);
        const compareResult2 = service.comparePaiementFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePaiementFrais(entity1, entity2);
        const compareResult2 = service.comparePaiementFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePaiementFrais(entity1, entity2);
        const compareResult2 = service.comparePaiementFrais(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
