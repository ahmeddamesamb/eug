import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormationPriveeComponent } from './list/formation-privee.component';
import { FormationPriveeDetailComponent } from './detail/formation-privee-detail.component';
import { FormationPriveeUpdateComponent } from './update/formation-privee-update.component';
import FormationPriveeResolve from './route/formation-privee-routing-resolve.service';

const formationPriveeRoute: Routes = [
  {
    path: '',
    component: FormationPriveeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationPriveeDetailComponent,
    resolve: {
      formationPrivee: FormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationPriveeUpdateComponent,
    resolve: {
      formationPrivee: FormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationPriveeUpdateComponent,
    resolve: {
      formationPrivee: FormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formationPriveeRoute;
