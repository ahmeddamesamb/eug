import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFrais } from '../frais.model';
import { FraisService } from '../service/frais.service';

export const fraisResolve = (route: ActivatedRouteSnapshot): Observable<null | IFrais> => {
  const id = route.params['id'];
  if (id) {
    return inject(FraisService)
      .find(id)
      .pipe(
        mergeMap((frais: HttpResponse<IFrais>) => {
          if (frais.body) {
            return of(frais.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default fraisResolve;
