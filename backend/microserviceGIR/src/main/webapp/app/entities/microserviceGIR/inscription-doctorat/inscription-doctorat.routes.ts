import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InscriptionDoctoratComponent } from './list/inscription-doctorat.component';
import { InscriptionDoctoratDetailComponent } from './detail/inscription-doctorat-detail.component';
import { InscriptionDoctoratUpdateComponent } from './update/inscription-doctorat-update.component';
import InscriptionDoctoratResolve from './route/inscription-doctorat-routing-resolve.service';

const inscriptionDoctoratRoute: Routes = [
  {
    path: '',
    component: InscriptionDoctoratComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InscriptionDoctoratDetailComponent,
    resolve: {
      inscriptionDoctorat: InscriptionDoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InscriptionDoctoratUpdateComponent,
    resolve: {
      inscriptionDoctorat: InscriptionDoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InscriptionDoctoratUpdateComponent,
    resolve: {
      inscriptionDoctorat: InscriptionDoctoratResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inscriptionDoctoratRoute;
