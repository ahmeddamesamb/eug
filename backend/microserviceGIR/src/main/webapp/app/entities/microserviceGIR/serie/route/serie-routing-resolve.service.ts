import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISerie } from '../serie.model';
import { SerieService } from '../service/serie.service';

export const serieResolve = (route: ActivatedRouteSnapshot): Observable<null | ISerie> => {
  const id = route.params['id'];
  if (id) {
    return inject(SerieService)
      .find(id)
      .pipe(
        mergeMap((serie: HttpResponse<ISerie>) => {
          if (serie.body) {
            return of(serie.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default serieResolve;
