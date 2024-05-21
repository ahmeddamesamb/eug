import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProgrammationInscriptionComponent } from './list/programmation-inscription.component';
import { ProgrammationInscriptionDetailComponent } from './detail/programmation-inscription-detail.component';
import { ProgrammationInscriptionUpdateComponent } from './update/programmation-inscription-update.component';
import ProgrammationInscriptionResolve from './route/programmation-inscription-routing-resolve.service';

const programmationInscriptionRoute: Routes = [
  {
    path: '',
    component: ProgrammationInscriptionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProgrammationInscriptionDetailComponent,
    resolve: {
      programmationInscription: ProgrammationInscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProgrammationInscriptionUpdateComponent,
    resolve: {
      programmationInscription: ProgrammationInscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProgrammationInscriptionUpdateComponent,
    resolve: {
      programmationInscription: ProgrammationInscriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default programmationInscriptionRoute;
