import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBaccalaureat } from '../baccalaureat.model';
import { BaccalaureatService } from '../service/baccalaureat.service';

export const baccalaureatResolve = (route: ActivatedRouteSnapshot): Observable<null | IBaccalaureat> => {
  const id = route.params['id'];
  if (id) {
    return inject(BaccalaureatService)
      .find(id)
      .pipe(
        mergeMap((baccalaureat: HttpResponse<IBaccalaureat>) => {
          if (baccalaureat.body) {
            return of(baccalaureat.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default baccalaureatResolve;
