import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'etudiant',
    data: { pageTitle: 'microserviceGirApp.microserviceGirEtudiant.home.title' },
    loadChildren: () => import('./microserviceGIR/etudiant/etudiant.routes'),
  },
  {
    path: 'paiement-formation-privee',
    data: { pageTitle: 'microserviceGirApp.microserviceGirPaiementFormationPrivee.home.title' },
    loadChildren: () => import('./microserviceGIR/paiement-formation-privee/paiement-formation-privee.routes'),
  },
  {
    path: 'information-personnelle',
    data: { pageTitle: 'microserviceGirApp.microserviceGirInformationPersonnelle.home.title' },
    loadChildren: () => import('./microserviceGIR/information-personnelle/information-personnelle.routes'),
  },
  {
    path: 'formation-privee',
    data: { pageTitle: 'microserviceGirApp.microserviceGirFormationPrivee.home.title' },
    loadChildren: () => import('./microserviceGIR/formation-privee/formation-privee.routes'),
  },
  {
    path: 'type-handicap',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeHandicap.home.title' },
    loadChildren: () => import('./microserviceGIR/type-handicap/type-handicap.routes'),
  },
  {
    path: 'type-bourse',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeBourse.home.title' },
    loadChildren: () => import('./microserviceGIR/type-bourse/type-bourse.routes'),
  },
  {
    path: 'region',
    data: { pageTitle: 'microserviceGirApp.microserviceGirRegion.home.title' },
    loadChildren: () => import('./microserviceGIR/region/region.routes'),
  },
  {
    path: 'pays',
    data: { pageTitle: 'microserviceGirApp.microserviceGirPays.home.title' },
    loadChildren: () => import('./microserviceGIR/pays/pays.routes'),
  },
  {
    path: 'zone',
    data: { pageTitle: 'microserviceGirApp.microserviceGirZone.home.title' },
    loadChildren: () => import('./microserviceGIR/zone/zone.routes'),
  },
  {
    path: 'lycee',
    data: { pageTitle: 'microserviceGirApp.microserviceGirLycee.home.title' },
    loadChildren: () => import('./microserviceGIR/lycee/lycee.routes'),
  },
  {
    path: 'baccalaureat',
    data: { pageTitle: 'microserviceGirApp.microserviceGirBaccalaureat.home.title' },
    loadChildren: () => import('./microserviceGIR/baccalaureat/baccalaureat.routes'),
  },
  {
    path: 'serie',
    data: { pageTitle: 'microserviceGirApp.microserviceGirSerie.home.title' },
    loadChildren: () => import('./microserviceGIR/serie/serie.routes'),
  },
  {
    path: 'type-selection',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeSelection.home.title' },
    loadChildren: () => import('./microserviceGIR/type-selection/type-selection.routes'),
  },
  {
    path: 'type-admission',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeAdmission.home.title' },
    loadChildren: () => import('./microserviceGIR/type-admission/type-admission.routes'),
  },
  {
    path: 'discipline-sportive-etudiant',
    data: { pageTitle: 'microserviceGirApp.microserviceGirDisciplineSportiveEtudiant.home.title' },
    loadChildren: () => import('./microserviceGIR/discipline-sportive-etudiant/discipline-sportive-etudiant.routes'),
  },
  {
    path: 'discipline-sportive',
    data: { pageTitle: 'microserviceGirApp.microserviceGirDisciplineSportive.home.title' },
    loadChildren: () => import('./microserviceGIR/discipline-sportive/discipline-sportive.routes'),
  },
  {
    path: 'annee-academique',
    data: { pageTitle: 'microserviceGirApp.microserviceGirAnneeAcademique.home.title' },
    loadChildren: () => import('./microserviceGIR/annee-academique/annee-academique.routes'),
  },
  {
    path: 'programmation-inscription',
    data: { pageTitle: 'microserviceGirApp.microserviceGirProgrammationInscription.home.title' },
    loadChildren: () => import('./microserviceGIR/programmation-inscription/programmation-inscription.routes'),
  },
  {
    path: 'inscription-administrative',
    data: { pageTitle: 'microserviceGirApp.microserviceGirInscriptionAdministrative.home.title' },
    loadChildren: () => import('./microserviceGIR/inscription-administrative/inscription-administrative.routes'),
  },
  {
    path: 'inscription-administrative-formation',
    data: { pageTitle: 'microserviceGirApp.microserviceGirInscriptionAdministrativeFormation.home.title' },
    loadChildren: () => import('./microserviceGIR/inscription-administrative-formation/inscription-administrative-formation.routes'),
  },
  {
    path: 'information-image',
    data: { pageTitle: 'microserviceGirApp.microserviceGirInformationImage.home.title' },
    loadChildren: () => import('./microserviceGIR/information-image/information-image.routes'),
  },
  {
    path: 'processus-inscription-administrative',
    data: { pageTitle: 'microserviceGirApp.microserviceGirProcessusInscriptionAdministrative.home.title' },
    loadChildren: () => import('./microserviceGIR/processus-inscription-administrative/processus-inscription-administrative.routes'),
  },
  {
    path: 'operation',
    data: { pageTitle: 'microserviceGirApp.microserviceGirOperation.home.title' },
    loadChildren: () => import('./microserviceGIR/operation/operation.routes'),
  },
  {
    path: 'type-operation',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeOperation.home.title' },
    loadChildren: () => import('./microserviceGIR/type-operation/type-operation.routes'),
  },
  {
    path: 'cycle',
    data: { pageTitle: 'microserviceGirApp.microserviceGirCycle.home.title' },
    loadChildren: () => import('./microserviceGIR/cycle/cycle.routes'),
  },
  {
    path: 'type-formation',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeFormation.home.title' },
    loadChildren: () => import('./microserviceGIR/type-formation/type-formation.routes'),
  },
  {
    path: 'specialite',
    data: { pageTitle: 'microserviceGirApp.microserviceGirSpecialite.home.title' },
    loadChildren: () => import('./microserviceGIR/specialite/specialite.routes'),
  },
  {
    path: 'niveau',
    data: { pageTitle: 'microserviceGirApp.microserviceGirNiveau.home.title' },
    loadChildren: () => import('./microserviceGIR/niveau/niveau.routes'),
  },
  {
    path: 'formation',
    data: { pageTitle: 'microserviceGirApp.microserviceGirFormation.home.title' },
    loadChildren: () => import('./microserviceGIR/formation/formation.routes'),
  },
  {
    path: 'formation-autorisee',
    data: { pageTitle: 'microserviceGirApp.microserviceGirFormationAutorisee.home.title' },
    loadChildren: () => import('./microserviceGIR/formation-autorisee/formation-autorisee.routes'),
  },
  {
    path: 'formation-invalide',
    data: { pageTitle: 'microserviceGirApp.microserviceGirFormationInvalide.home.title' },
    loadChildren: () => import('./microserviceGIR/formation-invalide/formation-invalide.routes'),
  },
  {
    path: 'mention',
    data: { pageTitle: 'microserviceGirApp.microserviceGirMention.home.title' },
    loadChildren: () => import('./microserviceGIR/mention/mention.routes'),
  },
  {
    path: 'domaine',
    data: { pageTitle: 'microserviceGirApp.microserviceGirDomaine.home.title' },
    loadChildren: () => import('./microserviceGIR/domaine/domaine.routes'),
  },
  {
    path: 'ufr',
    data: { pageTitle: 'microserviceGirApp.microserviceGirUfr.home.title' },
    loadChildren: () => import('./microserviceGIR/ufr/ufr.routes'),
  },
  {
    path: 'frais',
    data: { pageTitle: 'microserviceGirApp.microserviceGirFrais.home.title' },
    loadChildren: () => import('./microserviceGIR/frais/frais.routes'),
  },
  {
    path: 'type-frais',
    data: { pageTitle: 'microserviceGirApp.microserviceGirTypeFrais.home.title' },
    loadChildren: () => import('./microserviceGIR/type-frais/type-frais.routes'),
  },
  {
    path: 'paiement-frais',
    data: { pageTitle: 'microserviceGirApp.microserviceGirPaiementFrais.home.title' },
    loadChildren: () => import('./microserviceGIR/paiement-frais/paiement-frais.routes'),
  },
  {
    path: 'operateur',
    data: { pageTitle: 'microserviceGirApp.microserviceGirOperateur.home.title' },
    loadChildren: () => import('./microserviceGIR/operateur/operateur.routes'),
  },
  {
    path: 'campagne',
    data: { pageTitle: 'microserviceGirApp.microserviceGirCampagne.home.title' },
    loadChildren: () => import('./microserviceGIR/campagne/campagne.routes'),
  },
  {
    path: 'inscription-doctorat',
    data: { pageTitle: 'microserviceGirApp.microserviceGirInscriptionDoctorat.home.title' },
    loadChildren: () => import('./microserviceGIR/inscription-doctorat/inscription-doctorat.routes'),
  },
  {
    path: 'doctorat',
    data: { pageTitle: 'microserviceGirApp.microserviceGirDoctorat.home.title' },
    loadChildren: () => import('./microserviceGIR/doctorat/doctorat.routes'),
  },
  {
    path: 'universite',
    data: { pageTitle: 'microserviceGirApp.microserviceGirUniversite.home.title' },
    loadChildren: () => import('./microserviceGIR/universite/universite.routes'),
  },
  {
    path: 'ministere',
    data: { pageTitle: 'microserviceGirApp.microserviceGirMinistere.home.title' },
    loadChildren: () => import('./microserviceGIR/ministere/ministere.routes'),
  },
  {
    path: 'departement',
    data: { pageTitle: 'microserviceGirApp.microserviceGirDepartement.home.title' },
    loadChildren: () => import('./microserviceGIR/departement/departement.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
