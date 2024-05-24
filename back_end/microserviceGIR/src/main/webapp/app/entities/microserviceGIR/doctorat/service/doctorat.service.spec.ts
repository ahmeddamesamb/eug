import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDoctorat } from '../doctorat.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../doctorat.test-samples';

import { DoctoratService, RestDoctorat } from './doctorat.service';

const requireRestSample: RestDoctorat = {
  ...sampleWithRequiredData,
  anneeInscriptionDoctorat: sampleWithRequiredData.anneeInscriptionDoctorat?.format(DATE_FORMAT),
};

describe('Doctorat Service', () => {
  let service: DoctoratService;
  let httpMock: HttpTestingController;
  let expectedResult: IDoctorat | IDoctorat[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DoctoratService);
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

    it('should create a Doctorat', () => {
      const doctorat = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(doctorat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Doctorat', () => {
      const doctorat = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(doctorat).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Doctorat', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Doctorat', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Doctorat', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDoctoratToCollectionIfMissing', () => {
      it('should add a Doctorat to an empty array', () => {
        const doctorat: IDoctorat = sampleWithRequiredData;
        expectedResult = service.addDoctoratToCollectionIfMissing([], doctorat);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doctorat);
      });

      it('should not add a Doctorat to an array that contains it', () => {
        const doctorat: IDoctorat = sampleWithRequiredData;
        const doctoratCollection: IDoctorat[] = [
          {
            ...doctorat,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDoctoratToCollectionIfMissing(doctoratCollection, doctorat);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Doctorat to an array that doesn't contain it", () => {
        const doctorat: IDoctorat = sampleWithRequiredData;
        const doctoratCollection: IDoctorat[] = [sampleWithPartialData];
        expectedResult = service.addDoctoratToCollectionIfMissing(doctoratCollection, doctorat);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doctorat);
      });

      it('should add only unique Doctorat to an array', () => {
        const doctoratArray: IDoctorat[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const doctoratCollection: IDoctorat[] = [sampleWithRequiredData];
        expectedResult = service.addDoctoratToCollectionIfMissing(doctoratCollection, ...doctoratArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const doctorat: IDoctorat = sampleWithRequiredData;
        const doctorat2: IDoctorat = sampleWithPartialData;
        expectedResult = service.addDoctoratToCollectionIfMissing([], doctorat, doctorat2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(doctorat);
        expect(expectedResult).toContain(doctorat2);
      });

      it('should accept null and undefined values', () => {
        const doctorat: IDoctorat = sampleWithRequiredData;
        expectedResult = service.addDoctoratToCollectionIfMissing([], null, doctorat, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(doctorat);
      });

      it('should return initial array if no Doctorat is added', () => {
        const doctoratCollection: IDoctorat[] = [sampleWithRequiredData];
        expectedResult = service.addDoctoratToCollectionIfMissing(doctoratCollection, undefined, null);
        expect(expectedResult).toEqual(doctoratCollection);
      });
    });

    describe('compareDoctorat', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDoctorat(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDoctorat(entity1, entity2);
        const compareResult2 = service.compareDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDoctorat(entity1, entity2);
        const compareResult2 = service.compareDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDoctorat(entity1, entity2);
        const compareResult2 = service.compareDoctorat(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
