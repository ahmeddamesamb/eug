import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDocumentDelivre } from '../document-delivre.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../document-delivre.test-samples';

import { DocumentDelivreService, RestDocumentDelivre } from './document-delivre.service';

const requireRestSample: RestDocumentDelivre = {
  ...sampleWithRequiredData,
  anneeDoc: sampleWithRequiredData.anneeDoc?.toJSON(),
  dateEnregistrement: sampleWithRequiredData.dateEnregistrement?.toJSON(),
};

describe('DocumentDelivre Service', () => {
  let service: DocumentDelivreService;
  let httpMock: HttpTestingController;
  let expectedResult: IDocumentDelivre | IDocumentDelivre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DocumentDelivreService);
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

    it('should create a DocumentDelivre', () => {
      const documentDelivre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(documentDelivre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DocumentDelivre', () => {
      const documentDelivre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(documentDelivre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DocumentDelivre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DocumentDelivre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DocumentDelivre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a DocumentDelivre', () => {
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

    describe('addDocumentDelivreToCollectionIfMissing', () => {
      it('should add a DocumentDelivre to an empty array', () => {
        const documentDelivre: IDocumentDelivre = sampleWithRequiredData;
        expectedResult = service.addDocumentDelivreToCollectionIfMissing([], documentDelivre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentDelivre);
      });

      it('should not add a DocumentDelivre to an array that contains it', () => {
        const documentDelivre: IDocumentDelivre = sampleWithRequiredData;
        const documentDelivreCollection: IDocumentDelivre[] = [
          {
            ...documentDelivre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDocumentDelivreToCollectionIfMissing(documentDelivreCollection, documentDelivre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DocumentDelivre to an array that doesn't contain it", () => {
        const documentDelivre: IDocumentDelivre = sampleWithRequiredData;
        const documentDelivreCollection: IDocumentDelivre[] = [sampleWithPartialData];
        expectedResult = service.addDocumentDelivreToCollectionIfMissing(documentDelivreCollection, documentDelivre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentDelivre);
      });

      it('should add only unique DocumentDelivre to an array', () => {
        const documentDelivreArray: IDocumentDelivre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const documentDelivreCollection: IDocumentDelivre[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentDelivreToCollectionIfMissing(documentDelivreCollection, ...documentDelivreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const documentDelivre: IDocumentDelivre = sampleWithRequiredData;
        const documentDelivre2: IDocumentDelivre = sampleWithPartialData;
        expectedResult = service.addDocumentDelivreToCollectionIfMissing([], documentDelivre, documentDelivre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(documentDelivre);
        expect(expectedResult).toContain(documentDelivre2);
      });

      it('should accept null and undefined values', () => {
        const documentDelivre: IDocumentDelivre = sampleWithRequiredData;
        expectedResult = service.addDocumentDelivreToCollectionIfMissing([], null, documentDelivre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(documentDelivre);
      });

      it('should return initial array if no DocumentDelivre is added', () => {
        const documentDelivreCollection: IDocumentDelivre[] = [sampleWithRequiredData];
        expectedResult = service.addDocumentDelivreToCollectionIfMissing(documentDelivreCollection, undefined, null);
        expect(expectedResult).toEqual(documentDelivreCollection);
      });
    });

    describe('compareDocumentDelivre', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDocumentDelivre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDocumentDelivre(entity1, entity2);
        const compareResult2 = service.compareDocumentDelivre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDocumentDelivre(entity1, entity2);
        const compareResult2 = service.compareDocumentDelivre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDocumentDelivre(entity1, entity2);
        const compareResult2 = service.compareDocumentDelivre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
