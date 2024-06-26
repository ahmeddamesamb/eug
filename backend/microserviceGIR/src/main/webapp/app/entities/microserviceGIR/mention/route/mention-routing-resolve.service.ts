import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMention } from '../mention.model';
import { MentionService } from '../service/mention.service';

export const mentionResolve = (route: ActivatedRouteSnapshot): Observable<null | IMention> => {
  const id = route.params['id'];
  if (id) {
    return inject(MentionService)
      .find(id)
      .pipe(
        mergeMap((mention: HttpResponse<IMention>) => {
          if (mention.body) {
            return of(mention.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default mentionResolve;
