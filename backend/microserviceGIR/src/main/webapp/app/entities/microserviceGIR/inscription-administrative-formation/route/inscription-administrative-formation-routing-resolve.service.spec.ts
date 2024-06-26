import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from '../service/inscription-administrative-formation.service';

import inscriptionAdministrativeFormationResolve from './inscription-administrative-formation-routing-resolve.service';

describe('InscriptionAdministrativeFormation routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: InscriptionAdministrativeFormationService;
  let resultInscriptionAdministrativeFormation: IInscriptionAdministrativeFormation | null | undefined;

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
    service = TestBed.inject(InscriptionAdministrativeFormationService);
    resultInscriptionAdministrativeFormation = undefined;
  });

  describe('resolve', () => {
    it('should return IInscriptionAdministrativeFormation returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeFormationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrativeFormation = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionAdministrativeFormation).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeFormationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrativeFormation = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultInscriptionAdministrativeFormation).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IInscriptionAdministrativeFormation>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        inscriptionAdministrativeFormationResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultInscriptionAdministrativeFormation = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultInscriptionAdministrativeFormation).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
