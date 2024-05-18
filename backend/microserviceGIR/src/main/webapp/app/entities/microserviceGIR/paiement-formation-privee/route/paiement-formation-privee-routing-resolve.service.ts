import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementFormationPrivee } from '../paiement-formation-privee.model';
import { PaiementFormationPriveeService } from '../service/paiement-formation-privee.service';

export const paiementFormationPriveeResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaiementFormationPrivee> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaiementFormationPriveeService)
      .find(id)
      .pipe(
        mergeMap((paiementFormationPrivee: HttpResponse<IPaiementFormationPrivee>) => {
          if (paiementFormationPrivee.body) {
            return of(paiementFormationPrivee.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paiementFormationPriveeResolve;
