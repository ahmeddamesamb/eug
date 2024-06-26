import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaiementFormationPriveeComponent } from './list/paiement-formation-privee.component';
import { PaiementFormationPriveeDetailComponent } from './detail/paiement-formation-privee-detail.component';
import { PaiementFormationPriveeUpdateComponent } from './update/paiement-formation-privee-update.component';
import PaiementFormationPriveeResolve from './route/paiement-formation-privee-routing-resolve.service';

const paiementFormationPriveeRoute: Routes = [
  {
    path: '',
    component: PaiementFormationPriveeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementFormationPriveeDetailComponent,
    resolve: {
      paiementFormationPrivee: PaiementFormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementFormationPriveeUpdateComponent,
    resolve: {
      paiementFormationPrivee: PaiementFormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementFormationPriveeUpdateComponent,
    resolve: {
      paiementFormationPrivee: PaiementFormationPriveeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paiementFormationPriveeRoute;
