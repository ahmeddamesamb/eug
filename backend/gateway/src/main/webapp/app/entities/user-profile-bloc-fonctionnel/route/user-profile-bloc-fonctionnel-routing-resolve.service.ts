import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';
import { UserProfileBlocFonctionnelService } from '../service/user-profile-bloc-fonctionnel.service';

export const userProfileBlocFonctionnelResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserProfileBlocFonctionnel> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserProfileBlocFonctionnelService)
      .find(id)
      .pipe(
        mergeMap((userProfileBlocFonctionnel: HttpResponse<IUserProfileBlocFonctionnel>) => {
          if (userProfileBlocFonctionnel.body) {
            return of(userProfileBlocFonctionnel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default userProfileBlocFonctionnelResolve;
