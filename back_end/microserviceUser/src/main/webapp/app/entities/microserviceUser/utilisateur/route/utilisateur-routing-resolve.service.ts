import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUtilisateur } from '../utilisateur.model';
import { UtilisateurService } from '../service/utilisateur.service';

export const utilisateurResolve = (route: ActivatedRouteSnapshot): Observable<null | IUtilisateur> => {
  const id = route.params['id'];
  if (id) {
    return inject(UtilisateurService)
      .find(id)
      .pipe(
        mergeMap((utilisateur: HttpResponse<IUtilisateur>) => {
          if (utilisateur.body) {
            return of(utilisateur.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default utilisateurResolve;
