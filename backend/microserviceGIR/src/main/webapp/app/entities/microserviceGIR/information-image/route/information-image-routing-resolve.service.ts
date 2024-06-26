import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInformationImage } from '../information-image.model';
import { InformationImageService } from '../service/information-image.service';

export const informationImageResolve = (route: ActivatedRouteSnapshot): Observable<null | IInformationImage> => {
  const id = route.params['id'];
  if (id) {
    return inject(InformationImageService)
      .find(id)
      .pipe(
        mergeMap((informationImage: HttpResponse<IInformationImage>) => {
          if (informationImage.body) {
            return of(informationImage.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default informationImageResolve;
