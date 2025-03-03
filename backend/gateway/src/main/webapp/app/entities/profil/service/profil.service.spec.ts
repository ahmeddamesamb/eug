import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProfil } from '../profil.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../profil.test-samples';

import { ProfilService, RestProfil } from './profil.service';

const requireRestSample: RestProfil = {
  ...sampleWithRequiredData,
  dateAjout: sampleWithRequiredData.dateAjout?.format(DATE_FORMAT),
};

describe('Profil Service', () => {
  let service: ProfilService;
  let httpMock: HttpTestingController;
  let expectedResult: IProfil | IProfil[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProfilService);
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

    it('should create a Profil', () => {
      const profil = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(profil).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Profil', () => {
      const profil = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(profil).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Profil', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Profil', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Profil', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    it('should handle exceptions for searching a Profil', () => {
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

    describe('addProfilToCollectionIfMissing', () => {
      it('should add a Profil to an empty array', () => {
        const profil: IProfil = sampleWithRequiredData;
        expectedResult = service.addProfilToCollectionIfMissing([], profil);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profil);
      });

      it('should not add a Profil to an array that contains it', () => {
        const profil: IProfil = sampleWithRequiredData;
        const profilCollection: IProfil[] = [
          {
            ...profil,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, profil);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Profil to an array that doesn't contain it", () => {
        const profil: IProfil = sampleWithRequiredData;
        const profilCollection: IProfil[] = [sampleWithPartialData];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, profil);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profil);
      });

      it('should add only unique Profil to an array', () => {
        const profilArray: IProfil[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const profilCollection: IProfil[] = [sampleWithRequiredData];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, ...profilArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const profil: IProfil = sampleWithRequiredData;
        const profil2: IProfil = sampleWithPartialData;
        expectedResult = service.addProfilToCollectionIfMissing([], profil, profil2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profil);
        expect(expectedResult).toContain(profil2);
      });

      it('should accept null and undefined values', () => {
        const profil: IProfil = sampleWithRequiredData;
        expectedResult = service.addProfilToCollectionIfMissing([], null, profil, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profil);
      });

      it('should return initial array if no Profil is added', () => {
        const profilCollection: IProfil[] = [sampleWithRequiredData];
        expectedResult = service.addProfilToCollectionIfMissing(profilCollection, undefined, null);
        expect(expectedResult).toEqual(profilCollection);
      });
    });

    describe('compareProfil', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProfil(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProfil(entity1, entity2);
        const compareResult2 = service.compareProfil(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProfil(entity1, entity2);
        const compareResult2 = service.compareProfil(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProfil(entity1, entity2);
        const compareResult2 = service.compareProfil(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
