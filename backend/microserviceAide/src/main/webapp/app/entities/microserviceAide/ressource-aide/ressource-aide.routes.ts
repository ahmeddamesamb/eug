import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RessourceAideComponent } from './list/ressource-aide.component';
import { RessourceAideDetailComponent } from './detail/ressource-aide-detail.component';
import { RessourceAideUpdateComponent } from './update/ressource-aide-update.component';
import RessourceAideResolve from './route/ressource-aide-routing-resolve.service';

const ressourceAideRoute: Routes = [
  {
    path: '',
    component: RessourceAideComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RessourceAideDetailComponent,
    resolve: {
      ressourceAide: RessourceAideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RessourceAideUpdateComponent,
    resolve: {
      ressourceAide: RessourceAideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RessourceAideUpdateComponent,
    resolve: {
      ressourceAide: RessourceAideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ressourceAideRoute;
