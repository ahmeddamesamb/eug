import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeHandicapComponent } from './list/type-handicap.component';
import { TypeHandicapDetailComponent } from './detail/type-handicap-detail.component';
import { TypeHandicapUpdateComponent } from './update/type-handicap-update.component';
import TypeHandicapResolve from './route/type-handicap-routing-resolve.service';

const typeHandicapRoute: Routes = [
  {
    path: '',
    component: TypeHandicapComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeHandicapDetailComponent,
    resolve: {
      typeHandicap: TypeHandicapResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeHandicapUpdateComponent,
    resolve: {
      typeHandicap: TypeHandicapResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeHandicapUpdateComponent,
    resolve: {
      typeHandicap: TypeHandicapResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeHandicapRoute;
