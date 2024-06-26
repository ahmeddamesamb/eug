import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFormationAutorisee } from '../formation-autorisee.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../formation-autorisee.test-samples';

import { FormationAutoriseeService, RestFormationAutorisee } from './formation-autorisee.service';

const requireRestSample: RestFormationAutorisee = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('FormationAutorisee Service', () => {
  let service: FormationAutoriseeService;
  let httpMock: HttpTestingController;
  let expectedResult: IFormationAutorisee | IFormationAutorisee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationAutoriseeService);
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

    it('should create a FormationAutorisee', () => {
      const formationAutorisee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(formationAutorisee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationAutorisee', () => {
      const formationAutorisee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(formationAutorisee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationAutorisee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationAutorisee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FormationAutorisee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a FormationAutorisee', () => {
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

    describe('addFormationAutoriseeToCollectionIfMissing', () => {
      it('should add a FormationAutorisee to an empty array', () => {
        const formationAutorisee: IFormationAutorisee = sampleWithRequiredData;
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing([], formationAutorisee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationAutorisee);
      });

      it('should not add a FormationAutorisee to an array that contains it', () => {
        const formationAutorisee: IFormationAutorisee = sampleWithRequiredData;
        const formationAutoriseeCollection: IFormationAutorisee[] = [
          {
            ...formationAutorisee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing(formationAutoriseeCollection, formationAutorisee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationAutorisee to an array that doesn't contain it", () => {
        const formationAutorisee: IFormationAutorisee = sampleWithRequiredData;
        const formationAutoriseeCollection: IFormationAutorisee[] = [sampleWithPartialData];
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing(formationAutoriseeCollection, formationAutorisee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationAutorisee);
      });

      it('should add only unique FormationAutorisee to an array', () => {
        const formationAutoriseeArray: IFormationAutorisee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const formationAutoriseeCollection: IFormationAutorisee[] = [sampleWithRequiredData];
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing(formationAutoriseeCollection, ...formationAutoriseeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationAutorisee: IFormationAutorisee = sampleWithRequiredData;
        const formationAutorisee2: IFormationAutorisee = sampleWithPartialData;
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing([], formationAutorisee, formationAutorisee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationAutorisee);
        expect(expectedResult).toContain(formationAutorisee2);
      });

      it('should accept null and undefined values', () => {
        const formationAutorisee: IFormationAutorisee = sampleWithRequiredData;
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing([], null, formationAutorisee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationAutorisee);
      });

      it('should return initial array if no FormationAutorisee is added', () => {
        const formationAutoriseeCollection: IFormationAutorisee[] = [sampleWithRequiredData];
        expectedResult = service.addFormationAutoriseeToCollectionIfMissing(formationAutoriseeCollection, undefined, null);
        expect(expectedResult).toEqual(formationAutoriseeCollection);
      });
    });

    describe('compareFormationAutorisee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFormationAutorisee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareFormationAutorisee(entity1, entity2);
        const compareResult2 = service.compareFormationAutorisee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareFormationAutorisee(entity1, entity2);
        const compareResult2 = service.compareFormationAutorisee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareFormationAutorisee(entity1, entity2);
        const compareResult2 = service.compareFormationAutorisee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
