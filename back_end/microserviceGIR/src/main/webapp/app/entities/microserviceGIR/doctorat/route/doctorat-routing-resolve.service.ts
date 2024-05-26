import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDoctorat } from '../doctorat.model';
import { DoctoratService } from '../service/doctorat.service';

export const doctoratResolve = (route: ActivatedRouteSnapshot): Observable<null | IDoctorat> => {
  const id = route.params['id'];
  if (id) {
    return inject(DoctoratService)
      .find(id)
      .pipe(
        mergeMap((doctorat: HttpResponse<IDoctorat>) => {
          if (doctorat.body) {
            return of(doctorat.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default doctoratResolve;
