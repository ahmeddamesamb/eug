import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeFormationComponent } from './list/type-formation.component';
import { TypeFormationDetailComponent } from './detail/type-formation-detail.component';
import { TypeFormationUpdateComponent } from './update/type-formation-update.component';
import TypeFormationResolve from './route/type-formation-routing-resolve.service';

const typeFormationRoute: Routes = [
  {
    path: '',
    component: TypeFormationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeFormationDetailComponent,
    resolve: {
      typeFormation: TypeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeFormationUpdateComponent,
    resolve: {
      typeFormation: TypeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeFormationUpdateComponent,
    resolve: {
      typeFormation: TypeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeFormationRoute;
