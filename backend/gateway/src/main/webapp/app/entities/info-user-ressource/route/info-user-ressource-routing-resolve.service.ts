import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInfoUserRessource } from '../info-user-ressource.model';
import { InfoUserRessourceService } from '../service/info-user-ressource.service';

export const infoUserRessourceResolve = (route: ActivatedRouteSnapshot): Observable<null | IInfoUserRessource> => {
  const id = route.params['id'];
  if (id) {
    return inject(InfoUserRessourceService)
      .find(id)
      .pipe(
        mergeMap((infoUserRessource: HttpResponse<IInfoUserRessource>) => {
          if (infoUserRessource.body) {
            return of(infoUserRessource.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default infoUserRessourceResolve;
