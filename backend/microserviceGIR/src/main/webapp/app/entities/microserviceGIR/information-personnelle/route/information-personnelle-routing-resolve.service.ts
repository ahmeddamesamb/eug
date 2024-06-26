import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInformationPersonnelle } from '../information-personnelle.model';
import { InformationPersonnelleService } from '../service/information-personnelle.service';

export const informationPersonnelleResolve = (route: ActivatedRouteSnapshot): Observable<null | IInformationPersonnelle> => {
  const id = route.params['id'];
  if (id) {
    return inject(InformationPersonnelleService)
      .find(id)
      .pipe(
        mergeMap((informationPersonnelle: HttpResponse<IInformationPersonnelle>) => {
          if (informationPersonnelle.body) {
            return of(informationPersonnelle.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default informationPersonnelleResolve;
