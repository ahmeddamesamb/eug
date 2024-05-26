import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';
import { DisciplineSportiveEtudiantService } from '../service/discipline-sportive-etudiant.service';

export const disciplineSportiveEtudiantResolve = (route: ActivatedRouteSnapshot): Observable<null | IDisciplineSportiveEtudiant> => {
  const id = route.params['id'];
  if (id) {
    return inject(DisciplineSportiveEtudiantService)
      .find(id)
      .pipe(
        mergeMap((disciplineSportiveEtudiant: HttpResponse<IDisciplineSportiveEtudiant>) => {
          if (disciplineSportiveEtudiant.body) {
            return of(disciplineSportiveEtudiant.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default disciplineSportiveEtudiantResolve;
