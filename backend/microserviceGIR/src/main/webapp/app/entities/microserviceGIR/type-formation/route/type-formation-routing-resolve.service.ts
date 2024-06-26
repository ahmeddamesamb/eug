import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeFormation } from '../type-formation.model';
import { TypeFormationService } from '../service/type-formation.service';

export const typeFormationResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeFormation> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeFormationService)
      .find(id)
      .pipe(
        mergeMap((typeFormation: HttpResponse<ITypeFormation>) => {
          if (typeFormation.body) {
            return of(typeFormation.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeFormationResolve;
