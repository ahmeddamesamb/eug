import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoriqueConnexion } from '../historique-connexion.model';
import { HistoriqueConnexionService } from '../service/historique-connexion.service';

export const historiqueConnexionResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoriqueConnexion> => {
  const id = route.params['id'];
  if (id) {
    return inject(HistoriqueConnexionService)
      .find(id)
      .pipe(
        mergeMap((historiqueConnexion: HttpResponse<IHistoriqueConnexion>) => {
          if (historiqueConnexion.body) {
            return of(historiqueConnexion.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default historiqueConnexionResolve;
