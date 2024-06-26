import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomaine } from '../domaine.model';
import { DomaineService } from '../service/domaine.service';

export const domaineResolve = (route: ActivatedRouteSnapshot): Observable<null | IDomaine> => {
  const id = route.params['id'];
  if (id) {
    return inject(DomaineService)
      .find(id)
      .pipe(
        mergeMap((domaine: HttpResponse<IDomaine>) => {
          if (domaine.body) {
            return of(domaine.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default domaineResolve;
