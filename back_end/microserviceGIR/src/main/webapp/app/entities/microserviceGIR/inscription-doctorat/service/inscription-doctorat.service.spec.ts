import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../inscription-doctorat.test-samples';

import { InscriptionDoctoratService } from './inscription-doctorat.service';

const requireRestSample: IInscriptionDoctorat = {
  ...sampleWithRequiredData,
};

describe('InscriptionDoctorat Service', () => {
  let service: InscriptionDoctoratService;
  let httpMock: HttpTestingController;
  let expectedResult: IInscriptionDoctorat | IInscriptionDoctorat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InscriptionDoctoratService);
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

    it('should create a InscriptionDoctorat', () => {
      const inscriptionDoctorat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inscriptionDoctorat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InscriptionDoctorat', () => {
      const inscriptionDoctorat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inscriptionDoctorat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InscriptionDoctorat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InscriptionDoctorat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InscriptionDoctorat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInscriptionDoctoratToCollectionIfMissing', () => {
      it('should add a InscriptionDoctorat to an empty array', () => {
        const inscriptionDoctorat: IInscriptionDoctorat = sampleWithRequiredData;
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing([], inscriptionDoctorat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionDoctorat);
      });

      it('should not add a InscriptionDoctorat to an array that contains it', () => {
        const inscriptionDoctorat: IInscriptionDoctorat = sampleWithRequiredData;
        const inscriptionDoctoratCollection: IInscriptionDoctorat[] = [
          {
            ...inscriptionDoctorat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing(inscriptionDoctoratCollection, inscriptionDoctorat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InscriptionDoctorat to an array that doesn't contain it", () => {
        const inscriptionDoctorat: IInscriptionDoctorat = sampleWithRequiredData;
        const inscriptionDoctoratCollection: IInscriptionDoctorat[] = [sampleWithPartialData];
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing(inscriptionDoctoratCollection, inscriptionDoctorat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionDoctorat);
      });

      it('should add only unique InscriptionDoctorat to an array', () => {
        const inscriptionDoctoratArray: IInscriptionDoctorat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inscriptionDoctoratCollection: IInscriptionDoctorat[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing(inscriptionDoctoratCollection, ...inscriptionDoctoratArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscriptionDoctorat: IInscriptionDoctorat = sampleWithRequiredData;
        const inscriptionDoctorat2: IInscriptionDoctorat = sampleWithPartialData;
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing([], inscriptionDoctorat, inscriptionDoctorat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionDoctorat);
        expect(expectedResult).toContain(inscriptionDoctorat2);
      });

      it('should accept null and undefined values', () => {
        const inscriptionDoctorat: IInscriptionDoctorat = sampleWithRequiredData;
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing([], null, inscriptionDoctorat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionDoctorat);
      });

      it('should return initial array if no InscriptionDoctorat is added', () => {
        const inscriptionDoctoratCollection: IInscriptionDoctorat[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionDoctoratToCollectionIfMissing(inscriptionDoctoratCollection, undefined, null);
        expect(expectedResult).toEqual(inscriptionDoctoratCollection);
      });
    });

    describe('compareInscriptionDoctorat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInscriptionDoctorat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInscriptionDoctorat(entity1, entity2);
        const compareResult2 = service.compareInscriptionDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInscriptionDoctorat(entity1, entity2);
        const compareResult2 = service.compareInscriptionDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInscriptionDoctorat(entity1, entity2);
        const compareResult2 = service.compareInscriptionDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
