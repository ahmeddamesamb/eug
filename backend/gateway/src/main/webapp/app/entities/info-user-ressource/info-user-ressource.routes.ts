import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InfoUserRessourceComponent } from './list/info-user-ressource.component';
import { InfoUserRessourceDetailComponent } from './detail/info-user-ressource-detail.component';
import { InfoUserRessourceUpdateComponent } from './update/info-user-ressource-update.component';
import InfoUserRessourceResolve from './route/info-user-ressource-routing-resolve.service';

const infoUserRessourceRoute: Routes = [
  {
    path: '',
    component: InfoUserRessourceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfoUserRessourceDetailComponent,
    resolve: {
      infoUserRessource: InfoUserRessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfoUserRessourceUpdateComponent,
    resolve: {
      infoUserRessource: InfoUserRessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfoUserRessourceUpdateComponent,
    resolve: {
      infoUserRessource: InfoUserRessourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default infoUserRessourceRoute;
