import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeBourseComponent } from './list/type-bourse.component';
import { TypeBourseDetailComponent } from './detail/type-bourse-detail.component';
import { TypeBourseUpdateComponent } from './update/type-bourse-update.component';
import TypeBourseResolve from './route/type-bourse-routing-resolve.service';

const typeBourseRoute: Routes = [
  {
    path: '',
    component: TypeBourseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeBourseDetailComponent,
    resolve: {
      typeBourse: TypeBourseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeBourseUpdateComponent,
    resolve: {
      typeBourse: TypeBourseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeBourseUpdateComponent,
    resolve: {
      typeBourse: TypeBourseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeBourseRoute;
