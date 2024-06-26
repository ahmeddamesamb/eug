import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'enseignant',
    data: { pageTitle: 'microserviceGrhApp.microserviceGrhEnseignant.home.title' },
    loadChildren: () => import('./microserviceGRH/enseignant/enseignant.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
