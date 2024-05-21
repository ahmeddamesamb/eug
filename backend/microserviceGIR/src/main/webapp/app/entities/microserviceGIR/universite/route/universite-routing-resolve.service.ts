import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUniversite } from '../universite.model';
import { UniversiteService } from '../service/universite.service';

export const universiteResolve = (route: ActivatedRouteSnapshot): Observable<null | IUniversite> => {
  const id = route.params['id'];
  if (id) {
    return inject(UniversiteService)
      .find(id)
      .pipe(
        mergeMap((universite: HttpResponse<IUniversite>) => {
          if (universite.body) {
            return of(universite.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default universiteResolve;
