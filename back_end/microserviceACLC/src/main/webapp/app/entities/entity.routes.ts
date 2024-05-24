import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'candidat',
    data: { pageTitle: 'microserviceAclcApp.microserviceAclcCandidat.home.title' },
    loadChildren: () => import('./microserviceACLC/candidat/candidat.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
