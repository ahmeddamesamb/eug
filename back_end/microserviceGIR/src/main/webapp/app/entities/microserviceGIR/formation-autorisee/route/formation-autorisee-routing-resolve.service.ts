import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationAutorisee } from '../formation-autorisee.model';
import { FormationAutoriseeService } from '../service/formation-autorisee.service';

export const formationAutoriseeResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormationAutorisee> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormationAutoriseeService)
      .find(id)
      .pipe(
        mergeMap((formationAutorisee: HttpResponse<IFormationAutorisee>) => {
          if (formationAutorisee.body) {
            return of(formationAutorisee.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formationAutoriseeResolve;
