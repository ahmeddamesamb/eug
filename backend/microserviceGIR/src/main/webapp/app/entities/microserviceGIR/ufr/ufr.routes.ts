import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UfrComponent } from './list/ufr.component';
import { UfrDetailComponent } from './detail/ufr-detail.component';
import { UfrUpdateComponent } from './update/ufr-update.component';
import UfrResolve from './route/ufr-routing-resolve.service';

const ufrRoute: Routes = [
  {
    path: '',
    component: UfrComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UfrDetailComponent,
    resolve: {
      ufr: UfrResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UfrUpdateComponent,
    resolve: {
      ufr: UfrResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UfrUpdateComponent,
    resolve: {
      ufr: UfrResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ufrRoute;
