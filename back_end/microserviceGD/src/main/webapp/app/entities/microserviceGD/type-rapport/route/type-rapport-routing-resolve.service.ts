import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeRapport } from '../type-rapport.model';
import { TypeRapportService } from '../service/type-rapport.service';

export const typeRapportResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeRapport> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeRapportService)
      .find(id)
      .pipe(
        mergeMap((typeRapport: HttpResponse<ITypeRapport>) => {
          if (typeRapport.body) {
            return of(typeRapport.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeRapportResolve;
