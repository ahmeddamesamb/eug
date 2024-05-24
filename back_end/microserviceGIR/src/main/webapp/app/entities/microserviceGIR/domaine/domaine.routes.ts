import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DomaineComponent } from './list/domaine.component';
import { DomaineDetailComponent } from './detail/domaine-detail.component';
import { DomaineUpdateComponent } from './update/domaine-update.component';
import DomaineResolve from './route/domaine-routing-resolve.service';

const domaineRoute: Routes = [
  {
    path: '',
    component: DomaineComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomaineDetailComponent,
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomaineUpdateComponent,
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomaineUpdateComponent,
    resolve: {
      domaine: DomaineResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default domaineRoute;
