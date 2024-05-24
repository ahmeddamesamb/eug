import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProcessusDinscriptionAdministrativeComponent } from './list/processus-dinscription-administrative.component';
import { ProcessusDinscriptionAdministrativeDetailComponent } from './detail/processus-dinscription-administrative-detail.component';
import { ProcessusDinscriptionAdministrativeUpdateComponent } from './update/processus-dinscription-administrative-update.component';
import ProcessusDinscriptionAdministrativeResolve from './route/processus-dinscription-administrative-routing-resolve.service';

const processusDinscriptionAdministrativeRoute: Routes = [
  {
    path: '',
    component: ProcessusDinscriptionAdministrativeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProcessusDinscriptionAdministrativeDetailComponent,
    resolve: {
      processusDinscriptionAdministrative: ProcessusDinscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProcessusDinscriptionAdministrativeUpdateComponent,
    resolve: {
      processusDinscriptionAdministrative: ProcessusDinscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProcessusDinscriptionAdministrativeUpdateComponent,
    resolve: {
      processusDinscriptionAdministrative: ProcessusDinscriptionAdministrativeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default processusDinscriptionAdministrativeRoute;
