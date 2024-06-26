import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import { ProcessusInscriptionAdministrativeService } from '../service/processus-inscription-administrative.service';

import processusInscriptionAdministrativeResolve from './processus-inscription-administrative-routing-resolve.service';

describe('ProcessusInscriptionAdministrative routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: ProcessusInscriptionAdministrativeService;
  let resultProcessusInscriptionAdministrative: IProcessusInscriptionAdministrative | null | undefined;

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
    service = TestBed.inject(ProcessusInscriptionAdministrativeService);
    resultProcessusInscriptionAdministrative = undefined;
  });

  describe('resolve', () => {
    it('should return IProcessusInscriptionAdministrative returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        processusInscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultProcessusInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProcessusInscriptionAdministrative).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        processusInscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultProcessusInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultProcessusInscriptionAdministrative).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IProcessusInscriptionAdministrative>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        processusInscriptionAdministrativeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultProcessusInscriptionAdministrative = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultProcessusInscriptionAdministrative).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
