import { INavData } from '@coreui/angular-pro';

export const navItems: INavData[] = [
  {
    name: 'Accueil',
    url: '/dashboard',
    iconComponent: { name: '' },

  },
  {
    name: 'Scolarité',
    url: '/base',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Inscription',
        url: '/base/accordion',
        iconComponent: { name: 'cilEducation' },
        children: [
          {
            name: 'Etudiant',
            url: '/base/accordion',
            iconComponent: { name: 'cilFolderOpen' }
          },
          {
            name: 'Campagnes',
            url: '/base/breadcrumbs',
            iconComponent: { name: 'cilCalendar' }
          },
          {
            name: 'Déliberations',
            url: '/base/cards',
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
        url: '/base/accordion',
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
    url: '/base',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Paiements',
        url: '/base/accordion',
        iconComponent: { name: 'cilMoney' },
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Opérateurs',
            url: '/base/accordion',
            iconComponent: { name: 'cilTransfer' },
          },
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
        ]
      },
      
      {
        name: 'Répartitions',
        url: '/base/accordion',
        iconComponent: { name: 'cilGrain' },
      },
      {
        name: 'Statistiques',
        url: '/base/accordion',
        iconComponent: { name: 'cilChart' }
      },
      
    ]
    
  },
  {
    name: 'CROUS',
    url: '/base',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Visite médicale',
        url: '/base/accordion',
        iconComponent: { name: 'cilMedicalCross' },
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
          {
            name: 'A. Structure',
            url: '/base/accordion',
            iconComponent: { name: 'cilHouse' },
          },
          
        ]
      },
      {
        name: 'Liste inscrits',
        url: '/base/accordion',
        iconComponent: { name: 'cilPlaylistAdd' },
      },
      
      
    ]
    
  },

  {
    name: 'BU',
    url: '/base',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Quitus Etudiant',
        url: '/base/accordion',
        iconComponent: { name: 'cilSpreadsheet' },
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        iconComponent: { name: 'cilCog' },
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            iconComponent: { name: 'cilUser' },
          },
          
        ]
      },
      {
        name: 'Statistique',
        url: '/base/accordion',
        iconComponent: { name: 'cilChart' }
      },
      
      
    ]
    
  },






];
