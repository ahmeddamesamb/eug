import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormationInvalideComponent } from './list/formation-invalide.component';
import { FormationInvalideDetailComponent } from './detail/formation-invalide-detail.component';
import { FormationInvalideUpdateComponent } from './update/formation-invalide-update.component';
import FormationInvalideResolve from './route/formation-invalide-routing-resolve.service';

const formationInvalideRoute: Routes = [
  {
    path: '',
    component: FormationInvalideComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationInvalideDetailComponent,
    resolve: {
      formationInvalide: FormationInvalideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationInvalideUpdateComponent,
    resolve: {
      formationInvalide: FormationInvalideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationInvalideUpdateComponent,
    resolve: {
      formationInvalide: FormationInvalideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formationInvalideRoute;
