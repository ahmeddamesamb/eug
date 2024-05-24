import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInscriptionDoctorat } from '../inscription-doctorat.model';
import { InscriptionDoctoratService } from '../service/inscription-doctorat.service';

export const inscriptionDoctoratResolve = (route: ActivatedRouteSnapshot): Observable<null | IInscriptionDoctorat> => {
  const id = route.params['id'];
  if (id) {
    return inject(InscriptionDoctoratService)
      .find(id)
      .pipe(
        mergeMap((inscriptionDoctorat: HttpResponse<IInscriptionDoctorat>) => {
          if (inscriptionDoctorat.body) {
            return of(inscriptionDoctorat.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default inscriptionDoctoratResolve;
