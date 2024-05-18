import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { LyceeComponent } from './list/lycee.component';
import { LyceeDetailComponent } from './detail/lycee-detail.component';
import { LyceeUpdateComponent } from './update/lycee-update.component';
import LyceeResolve from './route/lycee-routing-resolve.service';

const lyceeRoute: Routes = [
  {
    path: '',
    component: LyceeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LyceeDetailComponent,
    resolve: {
      lycee: LyceeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LyceeUpdateComponent,
    resolve: {
      lycee: LyceeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LyceeUpdateComponent,
    resolve: {
      lycee: LyceeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default lyceeRoute;
