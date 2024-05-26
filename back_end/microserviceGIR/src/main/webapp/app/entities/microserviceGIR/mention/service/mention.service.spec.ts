import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMention } from '../mention.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../mention.test-samples';

import { MentionService } from './mention.service';

const requireRestSample: IMention = {
  ...sampleWithRequiredData,
};

describe('Mention Service', () => {
  let service: MentionService;
  let httpMock: HttpTestingController;
  let expectedResult: IMention | IMention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MentionService);
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

    it('should create a Mention', () => {
      const mention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mention', () => {
      const mention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMentionToCollectionIfMissing', () => {
      it('should add a Mention to an empty array', () => {
        const mention: IMention = sampleWithRequiredData;
        expectedResult = service.addMentionToCollectionIfMissing([], mention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mention);
      });

      it('should not add a Mention to an array that contains it', () => {
        const mention: IMention = sampleWithRequiredData;
        const mentionCollection: IMention[] = [
          {
            ...mention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMentionToCollectionIfMissing(mentionCollection, mention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mention to an array that doesn't contain it", () => {
        const mention: IMention = sampleWithRequiredData;
        const mentionCollection: IMention[] = [sampleWithPartialData];
        expectedResult = service.addMentionToCollectionIfMissing(mentionCollection, mention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mention);
      });

      it('should add only unique Mention to an array', () => {
        const mentionArray: IMention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mentionCollection: IMention[] = [sampleWithRequiredData];
        expectedResult = service.addMentionToCollectionIfMissing(mentionCollection, ...mentionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mention: IMention = sampleWithRequiredData;
        const mention2: IMention = sampleWithPartialData;
        expectedResult = service.addMentionToCollectionIfMissing([], mention, mention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mention);
        expect(expectedResult).toContain(mention2);
      });

      it('should accept null and undefined values', () => {
        const mention: IMention = sampleWithRequiredData;
        expectedResult = service.addMentionToCollectionIfMissing([], null, mention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mention);
      });

      it('should return initial array if no Mention is added', () => {
        const mentionCollection: IMention[] = [sampleWithRequiredData];
        expectedResult = service.addMentionToCollectionIfMissing(mentionCollection, undefined, null);
        expect(expectedResult).toEqual(mentionCollection);
      });
    });

    describe('compareMention', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMention(entity1, entity2);
        const compareResult2 = service.compareMention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMention(entity1, entity2);
        const compareResult2 = service.compareMention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMention(entity1, entity2);
        const compareResult2 = service.compareMention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
