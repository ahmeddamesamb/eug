import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRessource } from '../ressource.model';
import { RessourceService } from '../service/ressource.service';

export const ressourceResolve = (route: ActivatedRouteSnapshot): Observable<null | IRessource> => {
  const id = route.params['id'];
  if (id) {
    return inject(RessourceService)
      .find(id)
      .pipe(
        mergeMap((ressource: HttpResponse<IRessource>) => {
          if (ressource.body) {
            return of(ressource.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default ressourceResolve;
