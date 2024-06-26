import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeRapportComponent } from './list/type-rapport.component';
import { TypeRapportDetailComponent } from './detail/type-rapport-detail.component';
import { TypeRapportUpdateComponent } from './update/type-rapport-update.component';
import TypeRapportResolve from './route/type-rapport-routing-resolve.service';

const typeRapportRoute: Routes = [
  {
    path: '',
    component: TypeRapportComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeRapportDetailComponent,
    resolve: {
      typeRapport: TypeRapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeRapportUpdateComponent,
    resolve: {
      typeRapport: TypeRapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeRapportUpdateComponent,
    resolve: {
      typeRapport: TypeRapportResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeRapportRoute;
