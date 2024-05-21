import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUFR } from '../ufr.model';
import { UFRService } from '../service/ufr.service';

export const uFRResolve = (route: ActivatedRouteSnapshot): Observable<null | IUFR> => {
  const id = route.params['id'];
  if (id) {
    return inject(UFRService)
      .find(id)
      .pipe(
        mergeMap((uFR: HttpResponse<IUFR>) => {
          if (uFR.body) {
            return of(uFR.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default uFRResolve;
