import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TypeAdmissionComponent } from './list/type-admission.component';
import { TypeAdmissionDetailComponent } from './detail/type-admission-detail.component';
import { TypeAdmissionUpdateComponent } from './update/type-admission-update.component';
import TypeAdmissionResolve from './route/type-admission-routing-resolve.service';

const typeAdmissionRoute: Routes = [
  {
    path: '',
    component: TypeAdmissionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeAdmissionDetailComponent,
    resolve: {
      typeAdmission: TypeAdmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeAdmissionUpdateComponent,
    resolve: {
      typeAdmission: TypeAdmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeAdmissionUpdateComponent,
    resolve: {
      typeAdmission: TypeAdmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeAdmissionRoute;
