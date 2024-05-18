import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormationValideComponent } from './list/formation-valide.component';
import { FormationValideDetailComponent } from './detail/formation-valide-detail.component';
import { FormationValideUpdateComponent } from './update/formation-valide-update.component';
import FormationValideResolve from './route/formation-valide-routing-resolve.service';

const formationValideRoute: Routes = [
  {
    path: '',
    component: FormationValideComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationValideDetailComponent,
    resolve: {
      formationValide: FormationValideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationValideUpdateComponent,
    resolve: {
      formationValide: FormationValideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationValideUpdateComponent,
    resolve: {
      formationValide: FormationValideResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formationValideRoute;
