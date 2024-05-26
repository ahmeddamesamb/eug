import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UniversiteComponent } from './list/universite.component';
import { UniversiteDetailComponent } from './detail/universite-detail.component';
import { UniversiteUpdateComponent } from './update/universite-update.component';
import UniversiteResolve from './route/universite-routing-resolve.service';

const universiteRoute: Routes = [
  {
    path: '',
    component: UniversiteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UniversiteDetailComponent,
    resolve: {
      universite: UniversiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UniversiteUpdateComponent,
    resolve: {
      universite: UniversiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UniversiteUpdateComponent,
    resolve: {
      universite: UniversiteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default universiteRoute;
