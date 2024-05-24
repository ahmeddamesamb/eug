import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MentionComponent } from './list/mention.component';
import { MentionDetailComponent } from './detail/mention-detail.component';
import { MentionUpdateComponent } from './update/mention-update.component';
import MentionResolve from './route/mention-routing-resolve.service';

const mentionRoute: Routes = [
  {
    path: '',
    component: MentionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MentionDetailComponent,
    resolve: {
      mention: MentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MentionUpdateComponent,
    resolve: {
      mention: MentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MentionUpdateComponent,
    resolve: {
      mention: MentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mentionRoute;
