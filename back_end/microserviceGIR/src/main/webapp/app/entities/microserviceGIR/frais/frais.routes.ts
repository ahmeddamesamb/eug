import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FraisComponent } from './list/frais.component';
import { FraisDetailComponent } from './detail/frais-detail.component';
import { FraisUpdateComponent } from './update/frais-update.component';
import FraisResolve from './route/frais-routing-resolve.service';

const fraisRoute: Routes = [
  {
    path: '',
    component: FraisComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FraisDetailComponent,
    resolve: {
      frais: FraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FraisUpdateComponent,
    resolve: {
      frais: FraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FraisUpdateComponent,
    resolve: {
      frais: FraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default fraisRoute;
