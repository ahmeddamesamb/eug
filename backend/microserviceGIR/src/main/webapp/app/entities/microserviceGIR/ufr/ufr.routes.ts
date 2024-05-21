import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UFRComponent } from './list/ufr.component';
import { UFRDetailComponent } from './detail/ufr-detail.component';
import { UFRUpdateComponent } from './update/ufr-update.component';
import UFRResolve from './route/ufr-routing-resolve.service';

const uFRRoute: Routes = [
  {
    path: '',
    component: UFRComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UFRDetailComponent,
    resolve: {
      uFR: UFRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UFRUpdateComponent,
    resolve: {
      uFR: UFRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UFRUpdateComponent,
    resolve: {
      uFR: UFRResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default uFRRoute;
