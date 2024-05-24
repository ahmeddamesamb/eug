import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DisciplineSportiveEtudiantComponent } from './list/discipline-sportive-etudiant.component';
import { DisciplineSportiveEtudiantDetailComponent } from './detail/discipline-sportive-etudiant-detail.component';
import { DisciplineSportiveEtudiantUpdateComponent } from './update/discipline-sportive-etudiant-update.component';
import DisciplineSportiveEtudiantResolve from './route/discipline-sportive-etudiant-routing-resolve.service';

const disciplineSportiveEtudiantRoute: Routes = [
  {
    path: '',
    component: DisciplineSportiveEtudiantComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisciplineSportiveEtudiantDetailComponent,
    resolve: {
      disciplineSportiveEtudiant: DisciplineSportiveEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisciplineSportiveEtudiantUpdateComponent,
    resolve: {
      disciplineSportiveEtudiant: DisciplineSportiveEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisciplineSportiveEtudiantUpdateComponent,
    resolve: {
      disciplineSportiveEtudiant: DisciplineSportiveEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disciplineSportiveEtudiantRoute;
