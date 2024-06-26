import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BaccalaureatComponent } from './list/baccalaureat.component';
import { BaccalaureatDetailComponent } from './detail/baccalaureat-detail.component';
import { BaccalaureatUpdateComponent } from './update/baccalaureat-update.component';
import BaccalaureatResolve from './route/baccalaureat-routing-resolve.service';

const baccalaureatRoute: Routes = [
  {
    path: '',
    component: BaccalaureatComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BaccalaureatDetailComponent,
    resolve: {
      baccalaureat: BaccalaureatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BaccalaureatUpdateComponent,
    resolve: {
      baccalaureat: BaccalaureatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BaccalaureatUpdateComponent,
    resolve: {
      baccalaureat: BaccalaureatResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default baccalaureatRoute;
