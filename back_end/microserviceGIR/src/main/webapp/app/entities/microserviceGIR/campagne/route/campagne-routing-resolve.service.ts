import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICampagne } from '../campagne.model';
import { CampagneService } from '../service/campagne.service';

export const campagneResolve = (route: ActivatedRouteSnapshot): Observable<null | ICampagne> => {
  const id = route.params['id'];
  if (id) {
    return inject(CampagneService)
      .find(id)
      .pipe(
        mergeMap((campagne: HttpResponse<ICampagne>) => {
          if (campagne.body) {
            return of(campagne.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default campagneResolve;
