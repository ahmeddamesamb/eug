import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeliberation } from '../deliberation.model';
import { DeliberationService } from '../service/deliberation.service';

export const deliberationResolve = (route: ActivatedRouteSnapshot): Observable<null | IDeliberation> => {
  const id = route.params['id'];
  if (id) {
    return inject(DeliberationService)
      .find(id)
      .pipe(
        mergeMap((deliberation: HttpResponse<IDeliberation>) => {
          if (deliberation.body) {
            return of(deliberation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default deliberationResolve;
