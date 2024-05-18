import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';
import { InscriptionAdministrativeFormationService } from '../service/inscription-administrative-formation.service';

export const inscriptionAdministrativeFormationResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IInscriptionAdministrativeFormation> => {
  const id = route.params['id'];
  if (id) {
    return inject(InscriptionAdministrativeFormationService)
      .find(id)
      .pipe(
        mergeMap((inscriptionAdministrativeFormation: HttpResponse<IInscriptionAdministrativeFormation>) => {
          if (inscriptionAdministrativeFormation.body) {
            return of(inscriptionAdministrativeFormation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default inscriptionAdministrativeFormationResolve;
