import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProcessusInscriptionAdministrativeComponent } from './list/processus-inscription-administrative.component';
import { ProcessusInscriptionAdministrativeDetailComponent } from './detail/processus-inscription-administrative-detail.component';
import { ProcessusInscriptionAdministrativeUpdateComponent } from './update/processus-inscription-administrative-update.component';
import ProcessusInscriptionAdministrativeResolve from './route/processus-inscription-administrative-routing-resolve.service';

const processusInscriptionAdministrativeRoute: Routes = [
  {
    path: '',
    component: ProcessusInscriptionAdministrativeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcessusInscriptionAdministrativeDetailComponent,
    resolve: {
      processusInscriptionAdministrative: ProcessusInscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcessusInscriptionAdministrativeUpdateComponent,
    resolve: {
      processusInscriptionAdministrative: ProcessusInscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcessusInscriptionAdministrativeUpdateComponent,
    resolve: {
      processusInscriptionAdministrative: ProcessusInscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default processusInscriptionAdministrativeRoute;
