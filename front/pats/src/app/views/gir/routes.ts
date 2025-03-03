import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    data: {
      title: 'GIR'
    },
    children: [
        {
            path: '',
            redirectTo: 'parametrage',
            pathMatch: 'full'
        },
        {
            path: 'authentification',
            loadComponent: () => import('./authentification/authentification.component').then(m => m.AuthentificationComponent),
            data: {
                title: 'Authentification'
            }
        },
        {
            path: 'inscription-reinscription',
            loadComponent: () => import('./inscription-reinscription/inscription-reinscription.component').then(m => m.InscriptionReinscriptionComponent),
            data: {
                title: 'Inscription et Réinscription'
            },
            children: [
                {
                  path: '',
                  loadChildren: () => import('./inscription-reinscription/routes').then((m) => m.routes)
                },
                
            ]
        },
        {
            path: 'paiement-en-ligne-ou-presentiel',
            loadComponent: () => import('./payement/payement.component').then(m => m.PayementComponent),
            data: {
                title: 'Paiement en ligne ou présentiel'
            }
        },
        {
            path: 'gestion-campagne',
            loadComponent: () => import('./gestion-campagne/gestion-campagne.component').then(m => m.GestionCampagneComponent),
            data: {
                title: 'Gestion des Campagnes'
            },
            children: [
                {
                  path: '',
                  loadChildren: () => import('./gestion-campagne/routes').then((m) => m.routes)
                },
                
            ]
        },

        {
            path: 'gestion-formation',
            loadComponent: () => import('./gestion-formations/gestion-formations.component').then(m => m.GestionFormationsComponent),
            data: {
                title: 'Gestion des formations'
            },
            children: [
                {
                  path: '',
                  loadChildren: () => import('./gestion-formations/routes').then((m) => m.routes)
                },
                
            ]
        },
        {
            path: 'gestion-etudiant',
            loadComponent: () => import('./gestion-etudiants/gestion-etudiants.component').then(m => m.GestionEtudiantsComponent),
            data: {
                title: 'Gestion des étudiants'
            },
            children: [
                {
                  path: '',
                  loadChildren: () => import('./gestion-etudiants/routes').then((m) => m.routes)
                },
                
            ]
        },
        {
            path: 'parametrage',
            loadComponent: () => import('./parametrage/parametrage.component').then(m => m.ParametrageComponent),
            data: {
                title: 'Parametrage'
            },
            children: [
                {
                  path: '',
                  loadChildren: () => import('./parametrage/routes').then((m) => m.routes)
                },
                
            ]
        },
      
    ]
  }
];