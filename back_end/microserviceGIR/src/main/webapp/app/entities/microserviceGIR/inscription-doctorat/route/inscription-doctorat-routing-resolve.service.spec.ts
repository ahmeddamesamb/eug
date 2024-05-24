import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { InscriptionDoctoratService } from '../service/inscription-doctorat.service';

import inscriptionDoctoratResolve from './inscription-doctorat-routing-resolve.service';

describe('InscriptionDoctorat routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: InscriptionDoctoratService;
  let resultInscriptionDoctorat: IInscriptionDoctorat | null | undefined;

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
    service = TestBed.inject(InscriptionDoctoratService);
    resultInscriptionDoctorat = undefined;
  });

  describe('resolve', () => {
    it('should return IInscriptionDoctorat returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionDoctoratResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionDoctorat = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionDoctorat).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionDoctoratResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionDoctorat = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInscriptionDoctorat).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IInscriptionDoctorat>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionDoctoratResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionDoctorat = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionDoctorat).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
