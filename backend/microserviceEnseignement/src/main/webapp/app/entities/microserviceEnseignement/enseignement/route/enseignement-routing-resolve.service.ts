import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnseignement } from '../enseignement.model';
import { EnseignementService } from '../service/enseignement.service';

export const enseignementResolve = (route: ActivatedRouteSnapshot): Observable<null | IEnseignement> => {
  const id = route.params['id'];
  if (id) {
    return inject(EnseignementService)
      .find(id)
      .pipe(
        mergeMap((enseignement: HttpResponse<IEnseignement>) => {
          if (enseignement.body) {
            return of(enseignement.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default enseignementResolve;
