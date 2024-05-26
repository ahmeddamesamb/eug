import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanning } from '../planning.model';
import { PlanningService } from '../service/planning.service';

export const planningResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlanning> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlanningService)
      .find(id)
      .pipe(
        mergeMap((planning: HttpResponse<IPlanning>) => {
          if (planning.body) {
            return of(planning.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default planningResolve;
