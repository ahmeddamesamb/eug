import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInfosUser } from '../infos-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../infos-user.test-samples';

import { InfosUserService, RestInfosUser } from './infos-user.service';

const requireRestSample: RestInfosUser = {
  ...sampleWithRequiredData,
  dateAjout: sampleWithRequiredData.dateAjout?.format(DATE_FORMAT),
};

describe('InfosUser Service', () => {
  let service: InfosUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IInfosUser | IInfosUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InfosUserService);
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

    it('should create a InfosUser', () => {
      const infosUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(infosUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InfosUser', () => {
      const infosUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(infosUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InfosUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InfosUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InfosUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a InfosUser', () => {
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

    describe('addInfosUserToCollectionIfMissing', () => {
      it('should add a InfosUser to an empty array', () => {
        const infosUser: IInfosUser = sampleWithRequiredData;
        expectedResult = service.addInfosUserToCollectionIfMissing([], infosUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infosUser);
      });

      it('should not add a InfosUser to an array that contains it', () => {
        const infosUser: IInfosUser = sampleWithRequiredData;
        const infosUserCollection: IInfosUser[] = [
          {
            ...infosUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInfosUserToCollectionIfMissing(infosUserCollection, infosUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InfosUser to an array that doesn't contain it", () => {
        const infosUser: IInfosUser = sampleWithRequiredData;
        const infosUserCollection: IInfosUser[] = [sampleWithPartialData];
        expectedResult = service.addInfosUserToCollectionIfMissing(infosUserCollection, infosUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infosUser);
      });

      it('should add only unique InfosUser to an array', () => {
        const infosUserArray: IInfosUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const infosUserCollection: IInfosUser[] = [sampleWithRequiredData];
        expectedResult = service.addInfosUserToCollectionIfMissing(infosUserCollection, ...infosUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const infosUser: IInfosUser = sampleWithRequiredData;
        const infosUser2: IInfosUser = sampleWithPartialData;
        expectedResult = service.addInfosUserToCollectionIfMissing([], infosUser, infosUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(infosUser);
        expect(expectedResult).toContain(infosUser2);
      });

      it('should accept null and undefined values', () => {
        const infosUser: IInfosUser = sampleWithRequiredData;
        expectedResult = service.addInfosUserToCollectionIfMissing([], null, infosUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(infosUser);
      });

      it('should return initial array if no InfosUser is added', () => {
        const infosUserCollection: IInfosUser[] = [sampleWithRequiredData];
        expectedResult = service.addInfosUserToCollectionIfMissing(infosUserCollection, undefined, null);
        expect(expectedResult).toEqual(infosUserCollection);
      });
    });

    describe('compareInfosUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInfosUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInfosUser(entity1, entity2);
        const compareResult2 = service.compareInfosUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInfosUser(entity1, entity2);
        const compareResult2 = service.compareInfosUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInfosUser(entity1, entity2);
        const compareResult2 = service.compareInfosUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
