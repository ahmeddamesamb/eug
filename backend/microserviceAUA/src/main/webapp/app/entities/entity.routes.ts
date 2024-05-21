import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'laboratoire',
    data: { pageTitle: 'microserviceAuaApp.microserviceAuaLaboratoire.home.title' },
    loadChildren: () => import('./microserviceAUA/laboratoire/laboratoire.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
