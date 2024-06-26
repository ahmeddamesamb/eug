import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';
import { ProcessusInscriptionAdministrativeService } from '../service/processus-inscription-administrative.service';

export const processusInscriptionAdministrativeResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IProcessusInscriptionAdministrative> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProcessusInscriptionAdministrativeService)
      .find(id)
      .pipe(
        mergeMap((processusInscriptionAdministrative: HttpResponse<IProcessusInscriptionAdministrative>) => {
          if (processusInscriptionAdministrative.body) {
            return of(processusInscriptionAdministrative.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default processusInscriptionAdministrativeResolve;
