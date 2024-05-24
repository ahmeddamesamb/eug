import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DeliberationComponent } from './list/deliberation.component';
import { DeliberationDetailComponent } from './detail/deliberation-detail.component';
import { DeliberationUpdateComponent } from './update/deliberation-update.component';
import DeliberationResolve from './route/deliberation-routing-resolve.service';

const deliberationRoute: Routes = [
  {
    path: '',
    component: DeliberationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeliberationDetailComponent,
    resolve: {
      deliberation: DeliberationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DeliberationUpdateComponent,
    resolve: {
      deliberation: DeliberationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DeliberationUpdateComponent,
    resolve: {
      deliberation: DeliberationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default deliberationRoute;
