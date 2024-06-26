import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentDelivre } from '../document-delivre.model';
import { DocumentDelivreService } from '../service/document-delivre.service';

export const documentDelivreResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentDelivre> => {
  const id = route.params['id'];
  if (id) {
    return inject(DocumentDelivreService)
      .find(id)
      .pipe(
        mergeMap((documentDelivre: HttpResponse<IDocumentDelivre>) => {
          if (documentDelivre.body) {
            return of(documentDelivre.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default documentDelivreResolve;
