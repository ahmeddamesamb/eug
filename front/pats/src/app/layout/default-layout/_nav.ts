import { INavData } from '@coreui/angular-pro';

export const navItems: INavData[] = [
  {
    name: 'Accueil',
    url: '/dashboard',
    iconComponent: { name: '' },

  },
  {
    name: 'GIR',
    url: '/gir',
    iconComponent: { name: '' },
    children: [
      {
        name: 'Authentification',
        url: '/gir/authentification',
        iconComponent: { name: 'cilUser' }
      },
      {
        name: 'Inscription et Réinscription',
        url: '/gir/inscription-reinscription',
        iconComponent: { name: 'cilFolderOpen' }
      },

      {
        name: 'Paiement en ligne ou présentiel',
        url: '/gir/paiement-en-ligne-ou-presentiel',
        iconComponent: { name: 'cilMoney' }
      },
      {
        name: 'Gestion des formations',
        url: '/gir/gestion-formation',
        iconComponent: { name: 'cilClone' }
      },
      {
        name: 'Gestion des etudiant',
        url: '/gir/gestion-etudiant',
        iconComponent: { name: 'cilFolderOpen' }
      },
      /* {
        name: 'Impression',
        url: '/impression/accordion',
        iconComponent: { name: 'cilPrint' }
      }, */
      {
        name: 'Paramétrage',
        url: '/gir/parametrage',
        iconComponent: { name: 'cilCog' },
      },

    ]

  },

];
