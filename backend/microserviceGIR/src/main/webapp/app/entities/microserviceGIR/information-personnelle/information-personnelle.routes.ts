import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { InformationPersonnelleComponent } from './list/information-personnelle.component';
import { InformationPersonnelleDetailComponent } from './detail/information-personnelle-detail.component';
import { InformationPersonnelleUpdateComponent } from './update/information-personnelle-update.component';
import InformationPersonnelleResolve from './route/information-personnelle-routing-resolve.service';

const informationPersonnelleRoute: Routes = [
  {
    path: '',
    component: InformationPersonnelleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InformationPersonnelleDetailComponent,
    resolve: {
      informationPersonnelle: InformationPersonnelleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InformationPersonnelleUpdateComponent,
    resolve: {
      informationPersonnelle: InformationPersonnelleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InformationPersonnelleUpdateComponent,
    resolve: {
      informationPersonnelle: InformationPersonnelleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default informationPersonnelleRoute;
