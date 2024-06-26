import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInformationImage } from '../information-image.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../information-image.test-samples';

import { InformationImageService } from './information-image.service';

const requireRestSample: IInformationImage = {
  ...sampleWithRequiredData,
};

describe('InformationImage Service', () => {
  let service: InformationImageService;
  let httpMock: HttpTestingController;
  let expectedResult: IInformationImage | IInformationImage[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InformationImageService);
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

    it('should create a InformationImage', () => {
      const informationImage = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(informationImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InformationImage', () => {
      const informationImage = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(informationImage).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InformationImage', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InformationImage', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InformationImage', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InformationImage', () => {
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

    describe('addInformationImageToCollectionIfMissing', () => {
      it('should add a InformationImage to an empty array', () => {
        const informationImage: IInformationImage = sampleWithRequiredData;
        expectedResult = service.addInformationImageToCollectionIfMissing([], informationImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(informationImage);
      });

      it('should not add a InformationImage to an array that contains it', () => {
        const informationImage: IInformationImage = sampleWithRequiredData;
        const informationImageCollection: IInformationImage[] = [
          {
            ...informationImage,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInformationImageToCollectionIfMissing(informationImageCollection, informationImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InformationImage to an array that doesn't contain it", () => {
        const informationImage: IInformationImage = sampleWithRequiredData;
        const informationImageCollection: IInformationImage[] = [sampleWithPartialData];
        expectedResult = service.addInformationImageToCollectionIfMissing(informationImageCollection, informationImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(informationImage);
      });

      it('should add only unique InformationImage to an array', () => {
        const informationImageArray: IInformationImage[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const informationImageCollection: IInformationImage[] = [sampleWithRequiredData];
        expectedResult = service.addInformationImageToCollectionIfMissing(informationImageCollection, ...informationImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const informationImage: IInformationImage = sampleWithRequiredData;
        const informationImage2: IInformationImage = sampleWithPartialData;
        expectedResult = service.addInformationImageToCollectionIfMissing([], informationImage, informationImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(informationImage);
        expect(expectedResult).toContain(informationImage2);
      });

      it('should accept null and undefined values', () => {
        const informationImage: IInformationImage = sampleWithRequiredData;
        expectedResult = service.addInformationImageToCollectionIfMissing([], null, informationImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(informationImage);
      });

      it('should return initial array if no InformationImage is added', () => {
        const informationImageCollection: IInformationImage[] = [sampleWithRequiredData];
        expectedResult = service.addInformationImageToCollectionIfMissing(informationImageCollection, undefined, null);
        expect(expectedResult).toEqual(informationImageCollection);
      });
    });

    describe('compareInformationImage', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInformationImage(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInformationImage(entity1, entity2);
        const compareResult2 = service.compareInformationImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInformationImage(entity1, entity2);
        const compareResult2 = service.compareInformationImage(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInformationImage(entity1, entity2);
        const compareResult2 = service.compareInformationImage(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
