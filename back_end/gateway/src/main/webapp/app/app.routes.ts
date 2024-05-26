import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { errorRoute } from './layouts/error/error.route';

import HomeComponent from './home/home.component';
import NavbarComponent from './layouts/navbar/navbar.component';
import { loadEntityRoutes } from './core/microfrontend';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'home.title',
  },
  {
    path: '',
    component: NavbarComponent,
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'microserviceuser',
    loadChildren: () => loadEntityRoutes('microserviceuser'),
  },
  {
    path: 'microservicegir',
    loadChildren: () => loadEntityRoutes('microservicegir'),
  },
  {
    path: 'microserviceenseignement',
    loadChildren: () => loadEntityRoutes('microserviceenseignement'),
  },
  {
    path: 'microserviceedt',
    loadChildren: () => loadEntityRoutes('microserviceedt'),
  },
  {
    path: 'microservicegrh',
    loadChildren: () => loadEntityRoutes('microservicegrh'),
  },
  {
    path: 'microserviceaua',
    loadChildren: () => loadEntityRoutes('microserviceaua'),
  },
  {
    path: 'microservicedeliberation',
    loadChildren: () => loadEntityRoutes('microservicedeliberation'),
  },
  {
    path: 'microservicegd',
    loadChildren: () => loadEntityRoutes('microservicegd'),
  },
  {
    path: 'microserviceaclc',
    loadChildren: () => loadEntityRoutes('microserviceaclc'),
  },
  {
    path: 'microserviceaide',
    loadChildren: () => loadEntityRoutes('microserviceaide'),
  },
  {
    path: 'microserviceged',
    loadChildren: () => loadEntityRoutes('microserviceged'),
  },
  ...errorRoute,
];

export default routes;
