import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InscriptionAdministrativeComponent } from './list/inscription-administrative.component';
import { InscriptionAdministrativeDetailComponent } from './detail/inscription-administrative-detail.component';
import { InscriptionAdministrativeUpdateComponent } from './update/inscription-administrative-update.component';
import InscriptionAdministrativeResolve from './route/inscription-administrative-routing-resolve.service';

const inscriptionAdministrativeRoute: Routes = [
  {
    path: '',
    component: InscriptionAdministrativeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionAdministrativeDetailComponent,
    resolve: {
      inscriptionAdministrative: InscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionAdministrativeUpdateComponent,
    resolve: {
      inscriptionAdministrative: InscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionAdministrativeUpdateComponent,
    resolve: {
      inscriptionAdministrative: InscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inscriptionAdministrativeRoute;
