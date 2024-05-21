import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DocumentDelivreComponent } from './list/document-delivre.component';
import { DocumentDelivreDetailComponent } from './detail/document-delivre-detail.component';
import { DocumentDelivreUpdateComponent } from './update/document-delivre-update.component';
import DocumentDelivreResolve from './route/document-delivre-routing-resolve.service';

const documentDelivreRoute: Routes = [
  {
    path: '',
    component: DocumentDelivreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentDelivreDetailComponent,
    resolve: {
      documentDelivre: DocumentDelivreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentDelivreUpdateComponent,
    resolve: {
      documentDelivre: DocumentDelivreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentDelivreUpdateComponent,
    resolve: {
      documentDelivre: DocumentDelivreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default documentDelivreRoute;
