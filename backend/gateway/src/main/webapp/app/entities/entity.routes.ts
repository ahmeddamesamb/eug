import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'service-user',
    data: { pageTitle: 'gatewayApp.serviceUser.home.title' },
    loadChildren: () => import('./service-user/service-user.routes'),
  },
  {
    path: 'infos-user',
    data: { pageTitle: 'gatewayApp.infosUser.home.title' },
    loadChildren: () => import('./infos-user/infos-user.routes'),
  },
  {
    path: 'bloc-fonctionnel',
    data: { pageTitle: 'gatewayApp.blocFonctionnel.home.title' },
    loadChildren: () => import('./bloc-fonctionnel/bloc-fonctionnel.routes'),
  },
  {
    path: 'info-user-ressource',
    data: { pageTitle: 'gatewayApp.infoUserRessource.home.title' },
    loadChildren: () => import('./info-user-ressource/info-user-ressource.routes'),
  },
  {
    path: 'user-profile-bloc-fonctionnel',
    data: { pageTitle: 'gatewayApp.userProfileBlocFonctionnel.home.title' },
    loadChildren: () => import('./user-profile-bloc-fonctionnel/user-profile-bloc-fonctionnel.routes'),
  },
  {
    path: 'user-profile',
    data: { pageTitle: 'gatewayApp.userProfile.home.title' },
    loadChildren: () => import('./user-profile/user-profile.routes'),
  },
  {
    path: 'historique-connexion',
    data: { pageTitle: 'gatewayApp.historiqueConnexion.home.title' },
    loadChildren: () => import('./historique-connexion/historique-connexion.routes'),
  },
  {
    path: 'profil',
    data: { pageTitle: 'gatewayApp.profil.home.title' },
    loadChildren: () => import('./profil/profil.routes'),
  },
  {
    path: 'ressource',
    data: { pageTitle: 'gatewayApp.ressource.home.title' },
    loadChildren: () => import('./ressource/ressource.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
