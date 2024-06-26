import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeBourse } from '../type-bourse.model';
import { TypeBourseService } from '../service/type-bourse.service';

export const typeBourseResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeBourse> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeBourseService)
      .find(id)
      .pipe(
        mergeMap((typeBourse: HttpResponse<ITypeBourse>) => {
          if (typeBourse.body) {
            return of(typeBourse.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeBourseResolve;
