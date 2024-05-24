import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'deliberation',
    data: { pageTitle: 'microserviceDeliberationApp.microserviceDeliberationDeliberation.home.title' },
    loadChildren: () => import('./microserviceDeliberation/deliberation/deliberation.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
