import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBlocFonctionnel } from '../bloc-fonctionnel.model';
import { BlocFonctionnelService } from '../service/bloc-fonctionnel.service';

export const blocFonctionnelResolve = (route: ActivatedRouteSnapshot): Observable<null | IBlocFonctionnel> => {
  const id = route.params['id'];
  if (id) {
    return inject(BlocFonctionnelService)
      .find(id)
      .pipe(
        mergeMap((blocFonctionnel: HttpResponse<IBlocFonctionnel>) => {
          if (blocFonctionnel.body) {
            return of(blocFonctionnel.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default blocFonctionnelResolve;
