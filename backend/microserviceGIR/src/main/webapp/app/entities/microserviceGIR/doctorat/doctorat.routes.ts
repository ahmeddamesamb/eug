import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DoctoratComponent } from './list/doctorat.component';
import { DoctoratDetailComponent } from './detail/doctorat-detail.component';
import { DoctoratUpdateComponent } from './update/doctorat-update.component';
import DoctoratResolve from './route/doctorat-routing-resolve.service';

const doctoratRoute: Routes = [
  {
    path: '',
    component: DoctoratComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DoctoratDetailComponent,
    resolve: {
      doctorat: DoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DoctoratUpdateComponent,
    resolve: {
      doctorat: DoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DoctoratUpdateComponent,
    resolve: {
      doctorat: DoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default doctoratRoute;
