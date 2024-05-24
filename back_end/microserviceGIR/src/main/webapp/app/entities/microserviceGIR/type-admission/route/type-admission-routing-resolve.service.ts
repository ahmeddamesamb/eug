import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeAdmission } from '../type-admission.model';
import { TypeAdmissionService } from '../service/type-admission.service';

export const typeAdmissionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeAdmission> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeAdmissionService)
      .find(id)
      .pipe(
        mergeMap((typeAdmission: HttpResponse<ITypeAdmission>) => {
          if (typeAdmission.body) {
            return of(typeAdmission.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeAdmissionResolve;
