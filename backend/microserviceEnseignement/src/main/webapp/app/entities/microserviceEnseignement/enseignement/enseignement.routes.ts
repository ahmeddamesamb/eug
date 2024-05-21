import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EnseignementComponent } from './list/enseignement.component';
import { EnseignementDetailComponent } from './detail/enseignement-detail.component';
import { EnseignementUpdateComponent } from './update/enseignement-update.component';
import EnseignementResolve from './route/enseignement-routing-resolve.service';

const enseignementRoute: Routes = [
  {
    path: '',
    component: EnseignementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnseignementDetailComponent,
    resolve: {
      enseignement: EnseignementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnseignementUpdateComponent,
    resolve: {
      enseignement: EnseignementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnseignementUpdateComponent,
    resolve: {
      enseignement: EnseignementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enseignementRoute;
