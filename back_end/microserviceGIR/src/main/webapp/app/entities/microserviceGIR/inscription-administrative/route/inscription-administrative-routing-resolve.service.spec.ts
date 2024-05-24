import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInscriptionAdministrative } from '../inscription-administrative.model';
import { InscriptionAdministrativeService } from '../service/inscription-administrative.service';

import inscriptionAdministrativeResolve from './inscription-administrative-routing-resolve.service';

describe('InscriptionAdministrative routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: InscriptionAdministrativeService;
  let resultInscriptionAdministrative: IInscriptionAdministrative | null | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    service = TestBed.inject(InscriptionAdministrativeService);
    resultInscriptionAdministrative = undefined;
  });

  describe('resolve', () => {
    it('should return IInscriptionAdministrative returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionAdministrative).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInscriptionAdministrative).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IInscriptionAdministrative>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionAdministrative).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
