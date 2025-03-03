import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'ressource',
    data: { pageTitle: 'microserviceAideApp.microserviceAideRessource.home.title' },
    loadChildren: () => import('./microserviceAide/ressource/ressource.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
