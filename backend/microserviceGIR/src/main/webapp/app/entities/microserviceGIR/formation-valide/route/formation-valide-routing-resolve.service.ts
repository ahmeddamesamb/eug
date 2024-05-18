import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationValide } from '../formation-valide.model';
import { FormationValideService } from '../service/formation-valide.service';

export const formationValideResolve = (route: ActivatedRouteSnapshot): Observable<null | IFormationValide> => {
  const id = route.params['id'];
  if (id) {
    return inject(FormationValideService)
      .find(id)
      .pipe(
        mergeMap((formationValide: HttpResponse<IFormationValide>) => {
          if (formationValide.body) {
            return of(formationValide.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default formationValideResolve;
