import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcessusDinscriptionAdministrative } from '../processus-dinscription-administrative.model';
import { ProcessusDinscriptionAdministrativeService } from '../service/processus-dinscription-administrative.service';

export const processusDinscriptionAdministrativeResolve = (
  route: ActivatedRouteSnapshot,
): Observable<null | IProcessusDinscriptionAdministrative> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProcessusDinscriptionAdministrativeService)
      .find(id)
      .pipe(
        mergeMap((processusDinscriptionAdministrative: HttpResponse<IProcessusDinscriptionAdministrative>) => {
          if (processusDinscriptionAdministrative.body) {
            return of(processusDinscriptionAdministrative.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default processusDinscriptionAdministrativeResolve;
