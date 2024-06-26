import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'enseignement',
    data: { pageTitle: 'microserviceEnseignementApp.microserviceEnseignementEnseignement.home.title' },
    loadChildren: () => import('./microserviceEnseignement/enseignement/enseignement.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
