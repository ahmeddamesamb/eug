import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeSelection } from '../type-selection.model';
import { TypeSelectionService } from '../service/type-selection.service';

export const typeSelectionResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeSelection> => {
  const id = route.params['id'];
  if (id) {
    return inject(TypeSelectionService)
      .find(id)
      .pipe(
        mergeMap((typeSelection: HttpResponse<ITypeSelection>) => {
          if (typeSelection.body) {
            return of(typeSelection.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default typeSelectionResolve;
