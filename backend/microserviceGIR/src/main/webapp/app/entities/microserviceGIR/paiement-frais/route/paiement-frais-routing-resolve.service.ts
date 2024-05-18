import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPaiementFrais } from '../paiement-frais.model';
import { PaiementFraisService } from '../service/paiement-frais.service';

export const paiementFraisResolve = (route: ActivatedRouteSnapshot): Observable<null | IPaiementFrais> => {
  const id = route.params['id'];
  if (id) {
    return inject(PaiementFraisService)
      .find(id)
      .pipe(
        mergeMap((paiementFrais: HttpResponse<IPaiementFrais>) => {
          if (paiementFrais.body) {
            return of(paiementFrais.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default paiementFraisResolve;
