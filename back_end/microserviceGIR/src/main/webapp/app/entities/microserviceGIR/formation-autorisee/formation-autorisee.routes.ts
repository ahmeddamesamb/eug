import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormationAutoriseeComponent } from './list/formation-autorisee.component';
import { FormationAutoriseeDetailComponent } from './detail/formation-autorisee-detail.component';
import { FormationAutoriseeUpdateComponent } from './update/formation-autorisee-update.component';
import FormationAutoriseeResolve from './route/formation-autorisee-routing-resolve.service';

const formationAutoriseeRoute: Routes = [
  {
    path: '',
    component: FormationAutoriseeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationAutoriseeDetailComponent,
    resolve: {
      formationAutorisee: FormationAutoriseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationAutoriseeUpdateComponent,
    resolve: {
      formationAutorisee: FormationAutoriseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationAutoriseeUpdateComponent,
    resolve: {
      formationAutorisee: FormationAutoriseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formationAutoriseeRoute;
