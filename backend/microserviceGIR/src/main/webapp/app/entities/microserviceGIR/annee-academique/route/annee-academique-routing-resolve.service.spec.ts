import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

import anneeAcademiqueResolve from './annee-academique-routing-resolve.service';

describe('AnneeAcademique routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: AnneeAcademiqueService;
  let resultAnneeAcademique: IAnneeAcademique | null | undefined;

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
    service = TestBed.inject(AnneeAcademiqueService);
    resultAnneeAcademique = undefined;
  });

  describe('resolve', () => {
    it('should return IAnneeAcademique returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        anneeAcademiqueResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAnneeAcademique = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAnneeAcademique).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        anneeAcademiqueResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAnneeAcademique = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAnneeAcademique).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IAnneeAcademique>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        anneeAcademiqueResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultAnneeAcademique = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAnneeAcademique).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
