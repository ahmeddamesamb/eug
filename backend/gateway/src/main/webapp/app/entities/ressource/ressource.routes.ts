import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RessourceComponent } from './list/ressource.component';
import { RessourceDetailComponent } from './detail/ressource-detail.component';
import { RessourceUpdateComponent } from './update/ressource-update.component';
import RessourceResolve from './route/ressource-routing-resolve.service';

const ressourceRoute: Routes = [
  {
    path: '',
    component: RessourceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RessourceDetailComponent,
    resolve: {
      ressource: RessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RessourceUpdateComponent,
    resolve: {
      ressource: RessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RessourceUpdateComponent,
    resolve: {
      ressource: RessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ressourceRoute;
