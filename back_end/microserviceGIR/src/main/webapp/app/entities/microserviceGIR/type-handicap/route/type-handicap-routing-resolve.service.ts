import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeHandicap } from '../type-handicap.model';
import { TypeHandicapService } from '../service/type-handicap.service';

export const typeHandicapResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeHandicap> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeHandicapService)
      .find(id)
      .pipe(
        mergeMap((typeHandicap: HttpResponse<ITypeHandicap>) => {
          if (typeHandicap.body) {
            return of(typeHandicap.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeHandicapResolve;
