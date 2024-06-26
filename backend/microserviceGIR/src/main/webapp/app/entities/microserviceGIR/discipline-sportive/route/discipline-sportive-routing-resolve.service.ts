import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisciplineSportive } from '../discipline-sportive.model';
import { DisciplineSportiveService } from '../service/discipline-sportive.service';

export const disciplineSportiveResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisciplineSportive> => {
  const id = route.params['id'];
  if (id) {
    return inject(DisciplineSportiveService)
      .find(id)
      .pipe(
        mergeMap((disciplineSportive: HttpResponse<IDisciplineSportive>) => {
          if (disciplineSportive.body) {
            return of(disciplineSportive.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default disciplineSportiveResolve;
