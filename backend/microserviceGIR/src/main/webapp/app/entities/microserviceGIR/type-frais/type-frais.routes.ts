import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeFraisComponent } from './list/type-frais.component';
import { TypeFraisDetailComponent } from './detail/type-frais-detail.component';
import { TypeFraisUpdateComponent } from './update/type-frais-update.component';
import TypeFraisResolve from './route/type-frais-routing-resolve.service';

const typeFraisRoute: Routes = [
  {
    path: '',
    component: TypeFraisComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeFraisDetailComponent,
    resolve: {
      typeFrais: TypeFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeFraisUpdateComponent,
    resolve: {
      typeFrais: TypeFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeFraisUpdateComponent,
    resolve: {
      typeFrais: TypeFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeFraisRoute;
