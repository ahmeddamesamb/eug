import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NiveauComponent } from './list/niveau.component';
import { NiveauDetailComponent } from './detail/niveau-detail.component';
import { NiveauUpdateComponent } from './update/niveau-update.component';
import NiveauResolve from './route/niveau-routing-resolve.service';

const niveauRoute: Routes = [
  {
    path: '',
    component: NiveauComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NiveauDetailComponent,
    resolve: {
      niveau: NiveauResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NiveauUpdateComponent,
    resolve: {
      niveau: NiveauResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NiveauUpdateComponent,
    resolve: {
      niveau: NiveauResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default niveauRoute;
