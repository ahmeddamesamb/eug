import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';

export const profilResolve = (route: ActivatedRouteSnapshot): Observable<null | IProfil> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProfilService)
      .find(id)
      .pipe(
        mergeMap((profil: HttpResponse<IProfil>) => {
          if (profil.body) {
            return of(profil.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default profilResolve;
