import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IServiceUser } from '../service-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../service-user.test-samples';

import { ServiceUserService, RestServiceUser } from './service-user.service';

const requireRestSample: RestServiceUser = {
  ...sampleWithRequiredData,
  dateAjout: sampleWithRequiredData.dateAjout?.format(DATE_FORMAT),
};

describe('ServiceUser Service', () => {
  let service: ServiceUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IServiceUser | IServiceUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceUserService);
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

    it('should create a ServiceUser', () => {
      const serviceUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(serviceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceUser', () => {
      const serviceUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(serviceUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ServiceUser', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a ServiceUser', () => {
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

    describe('addServiceUserToCollectionIfMissing', () => {
      it('should add a ServiceUser to an empty array', () => {
        const serviceUser: IServiceUser = sampleWithRequiredData;
        expectedResult = service.addServiceUserToCollectionIfMissing([], serviceUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceUser);
      });

      it('should not add a ServiceUser to an array that contains it', () => {
        const serviceUser: IServiceUser = sampleWithRequiredData;
        const serviceUserCollection: IServiceUser[] = [
          {
            ...serviceUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addServiceUserToCollectionIfMissing(serviceUserCollection, serviceUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceUser to an array that doesn't contain it", () => {
        const serviceUser: IServiceUser = sampleWithRequiredData;
        const serviceUserCollection: IServiceUser[] = [sampleWithPartialData];
        expectedResult = service.addServiceUserToCollectionIfMissing(serviceUserCollection, serviceUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceUser);
      });

      it('should add only unique ServiceUser to an array', () => {
        const serviceUserArray: IServiceUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const serviceUserCollection: IServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addServiceUserToCollectionIfMissing(serviceUserCollection, ...serviceUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceUser: IServiceUser = sampleWithRequiredData;
        const serviceUser2: IServiceUser = sampleWithPartialData;
        expectedResult = service.addServiceUserToCollectionIfMissing([], serviceUser, serviceUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceUser);
        expect(expectedResult).toContain(serviceUser2);
      });

      it('should accept null and undefined values', () => {
        const serviceUser: IServiceUser = sampleWithRequiredData;
        expectedResult = service.addServiceUserToCollectionIfMissing([], null, serviceUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceUser);
      });

      it('should return initial array if no ServiceUser is added', () => {
        const serviceUserCollection: IServiceUser[] = [sampleWithRequiredData];
        expectedResult = service.addServiceUserToCollectionIfMissing(serviceUserCollection, undefined, null);
        expect(expectedResult).toEqual(serviceUserCollection);
      });
    });

    describe('compareServiceUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareServiceUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareServiceUser(entity1, entity2);
        const compareResult2 = service.compareServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareServiceUser(entity1, entity2);
        const compareResult2 = service.compareServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareServiceUser(entity1, entity2);
        const compareResult2 = service.compareServiceUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
