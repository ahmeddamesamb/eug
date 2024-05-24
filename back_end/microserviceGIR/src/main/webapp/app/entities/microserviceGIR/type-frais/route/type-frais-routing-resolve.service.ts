import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeFrais } from '../type-frais.model';
import { TypeFraisService } from '../service/type-frais.service';

export const typeFraisResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeFrais> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeFraisService)
      .find(id)
      .pipe(
        mergeMap((typeFrais: HttpResponse<ITypeFrais>) => {
          if (typeFrais.body) {
            return of(typeFrais.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeFraisResolve;
