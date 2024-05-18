import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InscriptionAdministrativeFormationComponent } from './list/inscription-administrative-formation.component';
import { InscriptionAdministrativeFormationDetailComponent } from './detail/inscription-administrative-formation-detail.component';
import { InscriptionAdministrativeFormationUpdateComponent } from './update/inscription-administrative-formation-update.component';
import InscriptionAdministrativeFormationResolve from './route/inscription-administrative-formation-routing-resolve.service';

const inscriptionAdministrativeFormationRoute: Routes = [
  {
    path: '',
    component: InscriptionAdministrativeFormationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionAdministrativeFormationDetailComponent,
    resolve: {
      inscriptionAdministrativeFormation: InscriptionAdministrativeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionAdministrativeFormationUpdateComponent,
    resolve: {
      inscriptionAdministrativeFormation: InscriptionAdministrativeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionAdministrativeFormationUpdateComponent,
    resolve: {
      inscriptionAdministrativeFormation: InscriptionAdministrativeFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inscriptionAdministrativeFormationRoute;
