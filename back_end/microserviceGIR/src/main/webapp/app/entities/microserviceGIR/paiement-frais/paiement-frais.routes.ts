import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PaiementFraisComponent } from './list/paiement-frais.component';
import { PaiementFraisDetailComponent } from './detail/paiement-frais-detail.component';
import { PaiementFraisUpdateComponent } from './update/paiement-frais-update.component';
import PaiementFraisResolve from './route/paiement-frais-routing-resolve.service';

const paiementFraisRoute: Routes = [
  {
    path: '',
    component: PaiementFraisComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PaiementFraisDetailComponent,
    resolve: {
      paiementFrais: PaiementFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PaiementFraisUpdateComponent,
    resolve: {
      paiementFrais: PaiementFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PaiementFraisUpdateComponent,
    resolve: {
      paiementFrais: PaiementFraisResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paiementFraisRoute;
