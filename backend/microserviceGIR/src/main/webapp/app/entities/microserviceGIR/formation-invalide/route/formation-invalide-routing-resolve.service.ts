import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationInvalide } from '../formation-invalide.model';
import { FormationInvalideService } from '../service/formation-invalide.service';

export const formationInvalideResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormationInvalide> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormationInvalideService)
      .find(id)
      .pipe(
        mergeMap((formationInvalide: HttpResponse<IFormationInvalide>) => {
          if (formationInvalide.body) {
            return of(formationInvalide.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formationInvalideResolve;
