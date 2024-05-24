import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'type-document',
    data: { pageTitle: 'microserviceGedApp.microserviceGedTypeDocument.home.title' },
    loadChildren: () => import('./microserviceGED/type-document/type-document.routes'),
  },
  {
    path: 'document-delivre',
    data: { pageTitle: 'microserviceGedApp.microserviceGedDocumentDelivre.home.title' },
    loadChildren: () => import('./microserviceGED/document-delivre/document-delivre.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
