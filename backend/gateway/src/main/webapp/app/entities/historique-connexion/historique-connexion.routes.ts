import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HistoriqueConnexionComponent } from './list/historique-connexion.component';
import { HistoriqueConnexionDetailComponent } from './detail/historique-connexion-detail.component';
import { HistoriqueConnexionUpdateComponent } from './update/historique-connexion-update.component';
import HistoriqueConnexionResolve from './route/historique-connexion-routing-resolve.service';

const historiqueConnexionRoute: Routes = [
  {
    path: '',
    component: HistoriqueConnexionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoriqueConnexionDetailComponent,
    resolve: {
      historiqueConnexion: HistoriqueConnexionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoriqueConnexionUpdateComponent,
    resolve: {
      historiqueConnexion: HistoriqueConnexionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoriqueConnexionUpdateComponent,
    resolve: {
      historiqueConnexion: HistoriqueConnexionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historiqueConnexionRoute;
