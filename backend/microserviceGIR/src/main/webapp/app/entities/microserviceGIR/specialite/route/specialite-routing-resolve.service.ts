import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpecialite } from '../specialite.model';
import { SpecialiteService } from '../service/specialite.service';

export const specialiteResolve = (route: ActivatedRouteSnapshot): Observable<null | ISpecialite> => {
  const id = route.params['id'];
  if (id) {
    return inject(SpecialiteService)
      .find(id)
      .pipe(
        mergeMap((specialite: HttpResponse<ISpecialite>) => {
          if (specialite.body) {
            return of(specialite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default specialiteResolve;
