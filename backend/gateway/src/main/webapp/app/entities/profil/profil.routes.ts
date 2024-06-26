import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProfilComponent } from './list/profil.component';
import { ProfilDetailComponent } from './detail/profil-detail.component';
import { ProfilUpdateComponent } from './update/profil-update.component';
import ProfilResolve from './route/profil-routing-resolve.service';

const profilRoute: Routes = [
  {
    path: '',
    component: ProfilComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProfilDetailComponent,
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProfilUpdateComponent,
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProfilUpdateComponent,
    resolve: {
      profil: ProfilResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default profilRoute;
