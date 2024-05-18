import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MinistereComponent } from './list/ministere.component';
import { MinistereDetailComponent } from './detail/ministere-detail.component';
import { MinistereUpdateComponent } from './update/ministere-update.component';
import MinistereResolve from './route/ministere-routing-resolve.service';

const ministereRoute: Routes = [
  {
    path: '',
    component: MinistereComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MinistereDetailComponent,
    resolve: {
      ministere: MinistereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MinistereUpdateComponent,
    resolve: {
      ministere: MinistereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MinistereUpdateComponent,
    resolve: {
      ministere: MinistereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ministereRoute;
