import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CampagneComponent } from './list/campagne.component';
import { CampagneDetailComponent } from './detail/campagne-detail.component';
import { CampagneUpdateComponent } from './update/campagne-update.component';
import CampagneResolve from './route/campagne-routing-resolve.service';

const campagneRoute: Routes = [
  {
    path: '',
    component: CampagneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CampagneDetailComponent,
    resolve: {
      campagne: CampagneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CampagneUpdateComponent,
    resolve: {
      campagne: CampagneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CampagneUpdateComponent,
    resolve: {
      campagne: CampagneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default campagneRoute;
