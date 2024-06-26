import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfosUser } from '../infos-user.model';
import { InfosUserService } from '../service/infos-user.service';

export const infosUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IInfosUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(InfosUserService)
      .find(id)
      .pipe(
        mergeMap((infosUser: HttpResponse<IInfosUser>) => {
          if (infosUser.body) {
            return of(infosUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default infosUserResolve;
