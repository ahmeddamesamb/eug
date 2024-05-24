import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILycee } from '../lycee.model';
import { LyceeService } from '../service/lycee.service';

export const lyceeResolve = (route: ActivatedRouteSnapshot): Observable<null | ILycee> => {
  const id = route.params['id'];
  if (id) {
    return inject(LyceeService)
      .find(id)
      .pipe(
        mergeMap((lycee: HttpResponse<ILycee>) => {
          if (lycee.body) {
            return of(lycee.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default lyceeResolve;
