import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'utilisateur',
    data: { pageTitle: 'microserviceUserApp.microserviceUserUtilisateur.home.title' },
    loadChildren: () => import('./microserviceUser/utilisateur/utilisateur.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
