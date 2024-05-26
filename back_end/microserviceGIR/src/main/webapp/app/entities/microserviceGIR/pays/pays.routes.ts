import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaysComponent } from './list/pays.component';
import { PaysDetailComponent } from './detail/pays-detail.component';
import { PaysUpdateComponent } from './update/pays-update.component';
import PaysResolve from './route/pays-routing-resolve.service';

const paysRoute: Routes = [
  {
    path: '',
    component: PaysComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaysDetailComponent,
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaysUpdateComponent,
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaysUpdateComponent,
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paysRoute;
