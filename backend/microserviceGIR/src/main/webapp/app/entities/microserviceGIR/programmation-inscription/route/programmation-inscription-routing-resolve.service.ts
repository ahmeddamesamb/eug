import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProgrammationInscription } from '../programmation-inscription.model';
import { ProgrammationInscriptionService } from '../service/programmation-inscription.service';

export const programmationInscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IProgrammationInscription> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProgrammationInscriptionService)
      .find(id)
      .pipe(
        mergeMap((programmationInscription: HttpResponse<IProgrammationInscription>) => {
          if (programmationInscription.body) {
            return of(programmationInscription.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default programmationInscriptionResolve;
