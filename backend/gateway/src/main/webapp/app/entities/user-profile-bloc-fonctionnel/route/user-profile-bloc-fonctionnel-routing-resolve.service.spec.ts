import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';
import { UserProfileBlocFonctionnelService } from '../service/user-profile-bloc-fonctionnel.service';

import userProfileBlocFonctionnelResolve from './user-profile-bloc-fonctionnel-routing-resolve.service';

describe('UserProfileBlocFonctionnel routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: UserProfileBlocFonctionnelService;
  let resultUserProfileBlocFonctionnel: IUserProfileBlocFonctionnel | null | undefined;

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
    service = TestBed.inject(UserProfileBlocFonctionnelService);
    resultUserProfileBlocFonctionnel = undefined;
  });

  describe('resolve', () => {
    it('should return IUserProfileBlocFonctionnel returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        userProfileBlocFonctionnelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUserProfileBlocFonctionnel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserProfileBlocFonctionnel).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      TestBed.runInInjectionContext(() => {
        userProfileBlocFonctionnelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUserProfileBlocFonctionnel = result;
          },
        });
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultUserProfileBlocFonctionnel).toEqual(null);
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<IUserProfileBlocFonctionnel>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      TestBed.runInInjectionContext(() => {
        userProfileBlocFonctionnelResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultUserProfileBlocFonctionnel = result;
          },
        });
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultUserProfileBlocFonctionnel).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
