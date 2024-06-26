import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DisciplineSportiveComponent } from './list/discipline-sportive.component';
import { DisciplineSportiveDetailComponent } from './detail/discipline-sportive-detail.component';
import { DisciplineSportiveUpdateComponent } from './update/discipline-sportive-update.component';
import DisciplineSportiveResolve from './route/discipline-sportive-routing-resolve.service';

const disciplineSportiveRoute: Routes = [
  {
    path: '',
    component: DisciplineSportiveComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisciplineSportiveDetailComponent,
    resolve: {
      disciplineSportive: DisciplineSportiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisciplineSportiveUpdateComponent,
    resolve: {
      disciplineSportive: DisciplineSportiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisciplineSportiveUpdateComponent,
    resolve: {
      disciplineSportive: DisciplineSportiveResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disciplineSportiveRoute;
