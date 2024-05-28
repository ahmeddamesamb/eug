import { INavData } from '@coreui/angular-pro';

export const navItems: INavData[] = [
  {
    name: 'Accueil',
    url: '/dashboard',
    iconComponent: { name: '' },

  },
  {
    name: 'Scolarité',
    url: '/scolarite',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Inscription',
        url: '/scolarite/inscription',
        iconComponent: { name: 'cilEducation' },
        children: [
          {
            name: 'Etudiant',
            url: '/etudiant',
            iconComponent: { name: 'cilFolderOpen' }
          },
          {
            name: 'Campagnes',
            url: '/scolarite/breadcrumbs',
            iconComponent: { name: 'cilCalendar' }
          },
          {
            name: 'Déliberations',
            url: '/scolarite/cards',
            iconComponent: { name: 'cilNotes' }
          },
        ]
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/accordion',
            iconComponent: { name: 'cilUser' },
          },
          {
            name: 'Etudiants',
            url: '/base/breadcrumbs',
            iconComponent: { name: 'cilFolderOpen' }
          },
          {
            name: 'Formation',
            url: '/base/cards',
            iconComponent: { name: 'cilClone' }
          },
        ]
      },
      {
        name: 'Impression',
        url: '/impression/accordion',
        iconComponent: { name: 'cilPrint' }
      },
      {
        name: 'Statistique',
        url: '/base/accordion',
        iconComponent: { name: 'cilChart' }
      },
      
    ]
    
  },
  {
    name: 'ACP',
    url: '/acp',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Paiements',
        url: '/paiements/accordion',
        iconComponent: { name: 'cilMoney' },
      },
      {
        name: 'Paramétrage',
        url: '/parametrage/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Opérateurs',
            url: '/parametrage/accordion',
            iconComponent: { name: 'cilTransfer' },
          },
          {
            name: 'Utilisateurs',
            url: '/parametrage/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
        ]
      },
      
      {
        name: 'Répartitions',
        url: '/repartitions/accordion',
        iconComponent: { name: 'cilGrain' },
      },
      {
        name: 'Statistiques',
        url: '/repartitions/accordion',
        iconComponent: { name: 'cilChart' }
      },
      
    ]
    
  },
  {
    name: 'CROUS',
    url: '/crous',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Visite médicale',
        url: '/visite-medicale/accordion',
        iconComponent: { name: 'cilMedicalCross' },
      },
      {
        name: 'Paramétrage',
        url: '/parametrage/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Utilisateurs',
            url: '/parametrage/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
          {
            name: 'A. Structure',
            url: '/parametrage/accordion',
            iconComponent: { name: 'cilHouse' },
          },
          
        ]
      },
      {
        name: 'Liste inscrits',
        url: '/liste-inscrits/accordion',
        iconComponent: { name: 'cilPlaylistAdd' },
      },
      
      
    ]
    
  },

  {
    name: 'BU',
    url: '/bu',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Quitus Etudiant',
        url: '/quitus-etudiant/accordion',
        iconComponent: { name: 'cilSpreadsheet' },
      },
      {
        name: 'Paramétrage',
        url: '/parametrage/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Utilisateurs',
            url: '/utilisateurs/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
          
        ]
      },
      {
        name: 'Statistique',
        url: '/statistique/accordion',
        iconComponent: { name: 'cilChart' }
      },
      
      
    ]
    
  },






];
