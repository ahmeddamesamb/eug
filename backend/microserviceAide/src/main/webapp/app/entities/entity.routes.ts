import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'ressource-aide',
    data: { pageTitle: 'microserviceAideApp.microserviceAideRessourceAide.home.title' },
    loadChildren: () => import('./microserviceAide/ressource-aide/ressource-aide.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
