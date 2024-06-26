import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InformationImageComponent } from './list/information-image.component';
import { InformationImageDetailComponent } from './detail/information-image-detail.component';
import { InformationImageUpdateComponent } from './update/information-image-update.component';
import InformationImageResolve from './route/information-image-routing-resolve.service';

const informationImageRoute: Routes = [
  {
    path: '',
    component: InformationImageComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InformationImageDetailComponent,
    resolve: {
      informationImage: InformationImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InformationImageUpdateComponent,
    resolve: {
      informationImage: InformationImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InformationImageUpdateComponent,
    resolve: {
      informationImage: InformationImageResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default informationImageRoute;
