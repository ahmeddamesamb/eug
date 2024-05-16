import { INavData } from '@coreui/angular-pro';

export const navItems: INavData[] = [
  {
    name: 'Accueil',
    url: '/dashboard',
    iconComponent: { name: 'cil-speedometer' },

  },
  {
    name: 'Scolarité',
    url: '/base',
    iconComponent: { name: 'cil-puzzle' },
    children: [
      {
        name: 'Inscription',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Etudiant',
            url: '/base/accordion',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Campagnes',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Déliberations',
            url: '/base/cards',
            icon: 'nav-icon-bullet'
          },
        ]
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/accordion',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Etudiants',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Formation',
            url: '/base/cards',
            icon: 'nav-icon-bullet'
          },
        ]
      },
      {
        name: 'Impression',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      {
        name: 'Statistique',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      
    ]
    
  },
  {
    name: 'ACP',
    url: '/base',
    iconComponent: { name: 'cil-puzzle' },
    children: [
      {
        name: 'Paiements',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Opérateurs',
            url: '/base/accordion',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
        ]
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/accordion',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Etudiants',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'Formation',
            url: '/base/cards',
            icon: 'nav-icon-bullet'
          },
        ]
      },
      
      {
        name: 'Répartitions',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      {
        name: 'Statistiques',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      
    ]
    
  },
  {
    name: 'CROUS',
    url: '/base',
    iconComponent: { name: 'cil-puzzle' },
    children: [
      {
        name: 'Visite médicale',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
          {
            name: 'A. Structure',
            url: '/base/accordion',
            icon: 'nav-icon-bullet'
          },
          
        ]
      },
      {
        name: 'Liste inscrits',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      
      
    ]
    
  },

  {
    name: 'BU',
    url: '/base',
    iconComponent: { name: 'cil-puzzle' },
    children: [
      {
        name: 'Quitus Etudiant',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      {
        name: 'Paramétrage',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
        children: [
          {
            name: 'Utilisateurs',
            url: '/base/breadcrumbs',
            icon: 'nav-icon-bullet'
          },
          
        ]
      },
      {
        name: 'Statistique',
        url: '/base/accordion',
        icon: 'nav-icon-bullet',
      },
      
      
    ]
    
  },






];
