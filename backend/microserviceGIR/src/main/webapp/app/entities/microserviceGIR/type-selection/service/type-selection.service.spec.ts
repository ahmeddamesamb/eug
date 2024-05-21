import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITypeSelection } from '../type-selection.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../type-selection.test-samples';

import { TypeSelectionService } from './type-selection.service';

const requireRestSample: ITypeSelection = {
  ...sampleWithRequiredData,
};

describe('TypeSelection Service', () => {
  let service: TypeSelectionService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeSelection | ITypeSelection[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TypeSelectionService);
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

    it('should create a TypeSelection', () => {
      const typeSelection = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeSelection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeSelection', () => {
      const typeSelection = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeSelection).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeSelection', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeSelection', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeSelection', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeSelectionToCollectionIfMissing', () => {
      it('should add a TypeSelection to an empty array', () => {
        const typeSelection: ITypeSelection = sampleWithRequiredData;
        expectedResult = service.addTypeSelectionToCollectionIfMissing([], typeSelection);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeSelection);
      });

      it('should not add a TypeSelection to an array that contains it', () => {
        const typeSelection: ITypeSelection = sampleWithRequiredData;
        const typeSelectionCollection: ITypeSelection[] = [
          {
            ...typeSelection,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeSelectionToCollectionIfMissing(typeSelectionCollection, typeSelection);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeSelection to an array that doesn't contain it", () => {
        const typeSelection: ITypeSelection = sampleWithRequiredData;
        const typeSelectionCollection: ITypeSelection[] = [sampleWithPartialData];
        expectedResult = service.addTypeSelectionToCollectionIfMissing(typeSelectionCollection, typeSelection);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeSelection);
      });

      it('should add only unique TypeSelection to an array', () => {
        const typeSelectionArray: ITypeSelection[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeSelectionCollection: ITypeSelection[] = [sampleWithRequiredData];
        expectedResult = service.addTypeSelectionToCollectionIfMissing(typeSelectionCollection, ...typeSelectionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeSelection: ITypeSelection = sampleWithRequiredData;
        const typeSelection2: ITypeSelection = sampleWithPartialData;
        expectedResult = service.addTypeSelectionToCollectionIfMissing([], typeSelection, typeSelection2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeSelection);
        expect(expectedResult).toContain(typeSelection2);
      });

      it('should accept null and undefined values', () => {
        const typeSelection: ITypeSelection = sampleWithRequiredData;
        expectedResult = service.addTypeSelectionToCollectionIfMissing([], null, typeSelection, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeSelection);
      });

      it('should return initial array if no TypeSelection is added', () => {
        const typeSelectionCollection: ITypeSelection[] = [sampleWithRequiredData];
        expectedResult = service.addTypeSelectionToCollectionIfMissing(typeSelectionCollection, undefined, null);
        expect(expectedResult).toEqual(typeSelectionCollection);
      });
    });

    describe('compareTypeSelection', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeSelection(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTypeSelection(entity1, entity2);
        const compareResult2 = service.compareTypeSelection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTypeSelection(entity1, entity2);
        const compareResult2 = service.compareTypeSelection(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTypeSelection(entity1, entity2);
        const compareResult2 = service.compareTypeSelection(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
