import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInscriptionAdministrative } from '../inscription-administrative.model';
import { InscriptionAdministrativeService } from '../service/inscription-administrative.service';

export const inscriptionAdministrativeResolve = (route: ActivatedRouteSnapshot): Observable<null | IInscriptionAdministrative> => {
  const id = route.params['id'];
  if (id) {
    return inject(InscriptionAdministrativeService)
      .find(id)
      .pipe(
        mergeMap((inscriptionAdministrative: HttpResponse<IInscriptionAdministrative>) => {
          if (inscriptionAdministrative.body) {
            return of(inscriptionAdministrative.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default inscriptionAdministrativeResolve;
