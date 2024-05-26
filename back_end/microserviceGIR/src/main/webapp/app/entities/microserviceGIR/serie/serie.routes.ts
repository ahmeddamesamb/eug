import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SerieComponent } from './list/serie.component';
import { SerieDetailComponent } from './detail/serie-detail.component';
import { SerieUpdateComponent } from './update/serie-update.component';
import SerieResolve from './route/serie-routing-resolve.service';

const serieRoute: Routes = [
  {
    path: '',
    component: SerieComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SerieDetailComponent,
    resolve: {
      serie: SerieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SerieUpdateComponent,
    resolve: {
      serie: SerieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SerieUpdateComponent,
    resolve: {
      serie: SerieResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default serieRoute;
