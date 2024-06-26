import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InfosUserComponent } from './list/infos-user.component';
import { InfosUserDetailComponent } from './detail/infos-user-detail.component';
import { InfosUserUpdateComponent } from './update/infos-user-update.component';
import InfosUserResolve from './route/infos-user-routing-resolve.service';

const infosUserRoute: Routes = [
  {
    path: '',
    component: InfosUserComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InfosUserDetailComponent,
    resolve: {
      infosUser: InfosUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InfosUserUpdateComponent,
    resolve: {
      infosUser: InfosUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InfosUserUpdateComponent,
    resolve: {
      infosUser: InfosUserResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default infosUserRoute;
