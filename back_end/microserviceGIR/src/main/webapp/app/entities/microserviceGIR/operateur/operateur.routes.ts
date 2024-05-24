import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { OperateurComponent } from './list/operateur.component';
import { OperateurDetailComponent } from './detail/operateur-detail.component';
import { OperateurUpdateComponent } from './update/operateur-update.component';
import OperateurResolve from './route/operateur-routing-resolve.service';

const operateurRoute: Routes = [
  {
    path: '',
    component: OperateurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OperateurDetailComponent,
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OperateurUpdateComponent,
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OperateurUpdateComponent,
    resolve: {
      operateur: OperateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default operateurRoute;
