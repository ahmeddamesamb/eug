import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'rapport',
    data: { pageTitle: 'microserviceGdApp.microserviceGdRapport.home.title' },
    loadChildren: () => import('./microserviceGD/rapport/rapport.routes'),
  },
  {
    path: 'type-rapport',
    data: { pageTitle: 'microserviceGdApp.microserviceGdTypeRapport.home.title' },
    loadChildren: () => import('./microserviceGD/type-rapport/type-rapport.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
