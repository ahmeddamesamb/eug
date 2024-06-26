import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRessourceAide } from '../ressource-aide.model';
import { RessourceAideService } from '../service/ressource-aide.service';

export const ressourceAideResolve = (route: ActivatedRouteSnapshot): Observable<null | IRessourceAide> => {
  const id = route.params['id'];
  if (id) {
    return inject(RessourceAideService)
      .find(id)
      .pipe(
        mergeMap((ressourceAide: HttpResponse<IRessourceAide>) => {
          if (ressourceAide.body) {
            return of(ressourceAide.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ressourceAideResolve;
