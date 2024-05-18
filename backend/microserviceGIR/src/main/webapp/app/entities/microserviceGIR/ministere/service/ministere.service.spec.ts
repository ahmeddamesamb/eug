import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IMinistere } from '../ministere.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../ministere.test-samples';

import { MinistereService, RestMinistere } from './ministere.service';

const requireRestSample: RestMinistere = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('Ministere Service', () => {
  let service: MinistereService;
  let httpMock: HttpTestingController;
  let expectedResult: IMinistere | IMinistere[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MinistereService);
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

    it('should create a Ministere', () => {
      const ministere = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ministere).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Ministere', () => {
      const ministere = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ministere).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Ministere', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Ministere', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Ministere', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMinistereToCollectionIfMissing', () => {
      it('should add a Ministere to an empty array', () => {
        const ministere: IMinistere = sampleWithRequiredData;
        expectedResult = service.addMinistereToCollectionIfMissing([], ministere);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministere);
      });

      it('should not add a Ministere to an array that contains it', () => {
        const ministere: IMinistere = sampleWithRequiredData;
        const ministereCollection: IMinistere[] = [
          {
            ...ministere,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMinistereToCollectionIfMissing(ministereCollection, ministere);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Ministere to an array that doesn't contain it", () => {
        const ministere: IMinistere = sampleWithRequiredData;
        const ministereCollection: IMinistere[] = [sampleWithPartialData];
        expectedResult = service.addMinistereToCollectionIfMissing(ministereCollection, ministere);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministere);
      });

      it('should add only unique Ministere to an array', () => {
        const ministereArray: IMinistere[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ministereCollection: IMinistere[] = [sampleWithRequiredData];
        expectedResult = service.addMinistereToCollectionIfMissing(ministereCollection, ...ministereArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ministere: IMinistere = sampleWithRequiredData;
        const ministere2: IMinistere = sampleWithPartialData;
        expectedResult = service.addMinistereToCollectionIfMissing([], ministere, ministere2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ministere);
        expect(expectedResult).toContain(ministere2);
      });

      it('should accept null and undefined values', () => {
        const ministere: IMinistere = sampleWithRequiredData;
        expectedResult = service.addMinistereToCollectionIfMissing([], null, ministere, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ministere);
      });

      it('should return initial array if no Ministere is added', () => {
        const ministereCollection: IMinistere[] = [sampleWithRequiredData];
        expectedResult = service.addMinistereToCollectionIfMissing(ministereCollection, undefined, null);
        expect(expectedResult).toEqual(ministereCollection);
      });
    });

    describe('compareMinistere', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMinistere(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMinistere(entity1, entity2);
        const compareResult2 = service.compareMinistere(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMinistere(entity1, entity2);
        const compareResult2 = service.compareMinistere(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMinistere(entity1, entity2);
        const compareResult2 = service.compareMinistere(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
