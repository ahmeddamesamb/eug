import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDisciplineSportive } from '../discipline-sportive.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../discipline-sportive.test-samples';

import { DisciplineSportiveService } from './discipline-sportive.service';

const requireRestSample: IDisciplineSportive = {
  ...sampleWithRequiredData,
};

describe('DisciplineSportive Service', () => {
  let service: DisciplineSportiveService;
  let httpMock: HttpTestingController;
  let expectedResult: IDisciplineSportive | IDisciplineSportive[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DisciplineSportiveService);
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

    it('should create a DisciplineSportive', () => {
      const disciplineSportive = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(disciplineSportive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DisciplineSportive', () => {
      const disciplineSportive = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(disciplineSportive).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DisciplineSportive', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DisciplineSportive', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DisciplineSportive', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DisciplineSportive', () => {
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

    describe('addDisciplineSportiveToCollectionIfMissing', () => {
      it('should add a DisciplineSportive to an empty array', () => {
        const disciplineSportive: IDisciplineSportive = sampleWithRequiredData;
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing([], disciplineSportive);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disciplineSportive);
      });

      it('should not add a DisciplineSportive to an array that contains it', () => {
        const disciplineSportive: IDisciplineSportive = sampleWithRequiredData;
        const disciplineSportiveCollection: IDisciplineSportive[] = [
          {
            ...disciplineSportive,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing(disciplineSportiveCollection, disciplineSportive);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DisciplineSportive to an array that doesn't contain it", () => {
        const disciplineSportive: IDisciplineSportive = sampleWithRequiredData;
        const disciplineSportiveCollection: IDisciplineSportive[] = [sampleWithPartialData];
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing(disciplineSportiveCollection, disciplineSportive);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disciplineSportive);
      });

      it('should add only unique DisciplineSportive to an array', () => {
        const disciplineSportiveArray: IDisciplineSportive[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const disciplineSportiveCollection: IDisciplineSportive[] = [sampleWithRequiredData];
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing(disciplineSportiveCollection, ...disciplineSportiveArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disciplineSportive: IDisciplineSportive = sampleWithRequiredData;
        const disciplineSportive2: IDisciplineSportive = sampleWithPartialData;
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing([], disciplineSportive, disciplineSportive2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disciplineSportive);
        expect(expectedResult).toContain(disciplineSportive2);
      });

      it('should accept null and undefined values', () => {
        const disciplineSportive: IDisciplineSportive = sampleWithRequiredData;
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing([], null, disciplineSportive, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disciplineSportive);
      });

      it('should return initial array if no DisciplineSportive is added', () => {
        const disciplineSportiveCollection: IDisciplineSportive[] = [sampleWithRequiredData];
        expectedResult = service.addDisciplineSportiveToCollectionIfMissing(disciplineSportiveCollection, undefined, null);
        expect(expectedResult).toEqual(disciplineSportiveCollection);
      });
    });

    describe('compareDisciplineSportive', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDisciplineSportive(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDisciplineSportive(entity1, entity2);
        const compareResult2 = service.compareDisciplineSportive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDisciplineSportive(entity1, entity2);
        const compareResult2 = service.compareDisciplineSportive(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDisciplineSportive(entity1, entity2);
        const compareResult2 = service.compareDisciplineSportive(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
