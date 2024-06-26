import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceUser } from '../service-user.model';
import { ServiceUserService } from '../service/service-user.service';

export const serviceUserResolve = (route: ActivatedRouteSnapshot): Observable<null | IServiceUser> => {
  const id = route.params['id'];
  if (id) {
    return inject(ServiceUserService)
      .find(id)
      .pipe(
        mergeMap((serviceUser: HttpResponse<IServiceUser>) => {
          if (serviceUser.body) {
            return of(serviceUser.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default serviceUserResolve;
