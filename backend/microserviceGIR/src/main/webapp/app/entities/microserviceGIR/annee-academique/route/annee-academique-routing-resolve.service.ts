import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueService } from '../service/annee-academique.service';

export const anneeAcademiqueResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnneeAcademique> => {
  const id = route.params['id'];
  if (id) {
    return inject(AnneeAcademiqueService)
      .find(id)
      .pipe(
        mergeMap((anneeAcademique: HttpResponse<IAnneeAcademique>) => {
          if (anneeAcademique.body) {
            return of(anneeAcademique.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default anneeAcademiqueResolve;
