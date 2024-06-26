import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import { PaiementFormationPriveeService } from '../service/paiement-formation-privee.service';

import paiementFormationPriveeResolve from './paiement-formation-privee-routing-resolve.service';

describe('PaiementFormationPrivee routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: PaiementFormationPriveeService;
  let resultPaiementFormationPrivee: IPaiementFormationPrivee | null | undefined;

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
    service = TestBed.inject(PaiementFormationPriveeService);
    resultPaiementFormationPrivee = undefined;
  });

  describe('resolve', () => {
    it('should return IPaiementFormationPrivee returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        paiementFormationPriveeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaiementFormationPrivee = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaiementFormationPrivee).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        paiementFormationPriveeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaiementFormationPrivee = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPaiementFormationPrivee).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IPaiementFormationPrivee>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        paiementFormationPriveeResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultPaiementFormationPrivee = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPaiementFormationPrivee).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
