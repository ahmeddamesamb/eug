import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AnneeAcademiqueComponent } from './list/annee-academique.component';
import { AnneeAcademiqueDetailComponent } from './detail/annee-academique-detail.component';
import { AnneeAcademiqueUpdateComponent } from './update/annee-academique-update.component';
import AnneeAcademiqueResolve from './route/annee-academique-routing-resolve.service';

const anneeAcademiqueRoute: Routes = [
  {
    path: '',
    component: AnneeAcademiqueComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnneeAcademiqueDetailComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnneeAcademiqueUpdateComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnneeAcademiqueUpdateComponent,
    resolve: {
      anneeAcademique: AnneeAcademiqueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default anneeAcademiqueRoute;
