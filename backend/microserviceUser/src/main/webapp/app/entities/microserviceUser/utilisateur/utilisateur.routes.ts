import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UtilisateurComponent } from './list/utilisateur.component';
import { UtilisateurDetailComponent } from './detail/utilisateur-detail.component';
import { UtilisateurUpdateComponent } from './update/utilisateur-update.component';
import UtilisateurResolve from './route/utilisateur-routing-resolve.service';

const utilisateurRoute: Routes = [
  {
    path: '',
    component: UtilisateurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UtilisateurDetailComponent,
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UtilisateurUpdateComponent,
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UtilisateurUpdateComponent,
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utilisateurRoute;
