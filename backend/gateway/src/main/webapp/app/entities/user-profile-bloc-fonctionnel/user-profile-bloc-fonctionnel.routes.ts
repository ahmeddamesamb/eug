import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { UserProfileBlocFonctionnelComponent } from './list/user-profile-bloc-fonctionnel.component';
import { UserProfileBlocFonctionnelDetailComponent } from './detail/user-profile-bloc-fonctionnel-detail.component';
import { UserProfileBlocFonctionnelUpdateComponent } from './update/user-profile-bloc-fonctionnel-update.component';
import UserProfileBlocFonctionnelResolve from './route/user-profile-bloc-fonctionnel-routing-resolve.service';

const userProfileBlocFonctionnelRoute: Routes = [
  {
    path: '',
    component: UserProfileBlocFonctionnelComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserProfileBlocFonctionnelDetailComponent,
    resolve: {
      userProfileBlocFonctionnel: UserProfileBlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserProfileBlocFonctionnelUpdateComponent,
    resolve: {
      userProfileBlocFonctionnel: UserProfileBlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserProfileBlocFonctionnelUpdateComponent,
    resolve: {
      userProfileBlocFonctionnel: UserProfileBlocFonctionnelResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userProfileBlocFonctionnelRoute;
