import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'planning',
    data: { pageTitle: 'microserviceEdtApp.microserviceEdtPlanning.home.title' },
    loadChildren: () => import('./microserviceEDT/planning/planning.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
