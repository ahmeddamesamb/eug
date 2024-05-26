import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUfr } from '../ufr.model';
import { UfrService } from '../service/ufr.service';

export const ufrResolve = (route: ActivatedRouteSnapshot): Observable<null | IUfr> => {
  const id = route.params['id'];
  if (id) {
    return inject(UfrService)
      .find(id)
      .pipe(
        mergeMap((ufr: HttpResponse<IUfr>) => {
          if (ufr.body) {
            return of(ufr.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ufrResolve;
