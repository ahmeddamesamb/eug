import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeSelectionComponent } from './list/type-selection.component';
import { TypeSelectionDetailComponent } from './detail/type-selection-detail.component';
import { TypeSelectionUpdateComponent } from './update/type-selection-update.component';
import TypeSelectionResolve from './route/type-selection-routing-resolve.service';

const typeSelectionRoute: Routes = [
  {
    path: '',
    component: TypeSelectionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeSelectionDetailComponent,
    resolve: {
      typeSelection: TypeSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeSelectionUpdateComponent,
    resolve: {
      typeSelection: TypeSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeSelectionUpdateComponent,
    resolve: {
      typeSelection: TypeSelectionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeSelectionRoute;
