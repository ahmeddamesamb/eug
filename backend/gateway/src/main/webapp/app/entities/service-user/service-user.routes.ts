import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ServiceUserComponent } from './list/service-user.component';
import { ServiceUserDetailComponent } from './detail/service-user-detail.component';
import { ServiceUserUpdateComponent } from './update/service-user-update.component';
import ServiceUserResolve from './route/service-user-routing-resolve.service';

const serviceUserRoute: Routes = [
  {
    path: '',
    component: ServiceUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceUserDetailComponent,
    resolve: {
      serviceUser: ServiceUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceUserUpdateComponent,
    resolve: {
      serviceUser: ServiceUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceUserUpdateComponent,
    resolve: {
      serviceUser: ServiceUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serviceUserRoute;
