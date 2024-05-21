import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMinistere } from '../ministere.model';
import { MinistereService } from '../service/ministere.service';

export const ministereResolve = (route: ActivatedRouteSnapshot): Observable<null | IMinistere> => {
  const id = route.params['id'];
  if (id) {
    return inject(MinistereService)
      .find(id)
      .pipe(
        mergeMap((ministere: HttpResponse<IMinistere>) => {
          if (ministere.body) {
            return of(ministere.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ministereResolve;
