import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ZoneComponent } from './list/zone.component';
import { ZoneDetailComponent } from './detail/zone-detail.component';
import { ZoneUpdateComponent } from './update/zone-update.component';
import ZoneResolve from './route/zone-routing-resolve.service';

const zoneRoute: Routes = [
  {
    path: '',
    component: ZoneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ZoneDetailComponent,
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ZoneUpdateComponent,
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ZoneUpdateComponent,
    resolve: {
      zone: ZoneResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default zoneRoute;
