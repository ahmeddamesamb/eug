import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BlocFonctionnelComponent } from './list/bloc-fonctionnel.component';
import { BlocFonctionnelDetailComponent } from './detail/bloc-fonctionnel-detail.component';
import { BlocFonctionnelUpdateComponent } from './update/bloc-fonctionnel-update.component';
import BlocFonctionnelResolve from './route/bloc-fonctionnel-routing-resolve.service';

const blocFonctionnelRoute: Routes = [
  {
    path: '',
    component: BlocFonctionnelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BlocFonctionnelDetailComponent,
    resolve: {
      blocFonctionnel: BlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BlocFonctionnelUpdateComponent,
    resolve: {
      blocFonctionnel: BlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BlocFonctionnelUpdateComponent,
    resolve: {
      blocFonctionnel: BlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default blocFonctionnelRoute;
