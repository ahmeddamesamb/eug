import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SpecialiteComponent } from './list/specialite.component';
import { SpecialiteDetailComponent } from './detail/specialite-detail.component';
import { SpecialiteUpdateComponent } from './update/specialite-update.component';
import SpecialiteResolve from './route/specialite-routing-resolve.service';

const specialiteRoute: Routes = [
  {
    path: '',
    component: SpecialiteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpecialiteDetailComponent,
    resolve: {
      specialite: SpecialiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpecialiteUpdateComponent,
    resolve: {
      specialite: SpecialiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpecialiteUpdateComponent,
    resolve: {
      specialite: SpecialiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default specialiteRoute;
