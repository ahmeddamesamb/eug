import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFrais } from '../frais.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../frais.test-samples';

import { FraisService, RestFrais } from './frais.service';

const requireRestSample: RestFrais = {
  ...sampleWithRequiredData,
  dateApplication: sampleWithRequiredData.dateApplication?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('Frais Service', () => {
  let service: FraisService;
  let httpMock: HttpTestingController;
  let expectedResult: IFrais | IFrais[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FraisService);
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

    it('should create a Frais', () => {
      const frais = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(frais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Frais', () => {
      const frais = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(frais).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Frais', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Frais', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Frais', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFraisToCollectionIfMissing', () => {
      it('should add a Frais to an empty array', () => {
        const frais: IFrais = sampleWithRequiredData;
        expectedResult = service.addFraisToCollectionIfMissing([], frais);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frais);
      });

      it('should not add a Frais to an array that contains it', () => {
        const frais: IFrais = sampleWithRequiredData;
        const fraisCollection: IFrais[] = [
          {
            ...frais,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFraisToCollectionIfMissing(fraisCollection, frais);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Frais to an array that doesn't contain it", () => {
        const frais: IFrais = sampleWithRequiredData;
        const fraisCollection: IFrais[] = [sampleWithPartialData];
        expectedResult = service.addFraisToCollectionIfMissing(fraisCollection, frais);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frais);
      });

      it('should add only unique Frais to an array', () => {
        const fraisArray: IFrais[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const fraisCollection: IFrais[] = [sampleWithRequiredData];
        expectedResult = service.addFraisToCollectionIfMissing(fraisCollection, ...fraisArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const frais: IFrais = sampleWithRequiredData;
        const frais2: IFrais = sampleWithPartialData;
        expectedResult = service.addFraisToCollectionIfMissing([], frais, frais2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(frais);
        expect(expectedResult).toContain(frais2);
      });

      it('should accept null and undefined values', () => {
        const frais: IFrais = sampleWithRequiredData;
        expectedResult = service.addFraisToCollectionIfMissing([], null, frais, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(frais);
      });

      it('should return initial array if no Frais is added', () => {
        const fraisCollection: IFrais[] = [sampleWithRequiredData];
        expectedResult = service.addFraisToCollectionIfMissing(fraisCollection, undefined, null);
        expect(expectedResult).toEqual(fraisCollection);
      });
    });

    describe('compareFrais', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFrais(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFrais(entity1, entity2);
        const compareResult2 = service.compareFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFrais(entity1, entity2);
        const compareResult2 = service.compareFrais(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFrais(entity1, entity2);
        const compareResult2 = service.compareFrais(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
