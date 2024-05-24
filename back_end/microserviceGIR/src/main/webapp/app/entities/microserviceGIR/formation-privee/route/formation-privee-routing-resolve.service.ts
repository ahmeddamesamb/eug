import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationPrivee } from '../formation-privee.model';
import { FormationPriveeService } from '../service/formation-privee.service';

export const formationPriveeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormationPrivee> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormationPriveeService)
      .find(id)
      .pipe(
        mergeMap((formationPrivee: HttpResponse<IFormationPrivee>) => {
          if (formationPrivee.body) {
            return of(formationPrivee.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formationPriveeResolve;
