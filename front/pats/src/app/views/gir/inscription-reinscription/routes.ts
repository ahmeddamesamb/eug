import { Routes } from '@angular/router';

export const routes: Routes = [

 /*  {
    path: '',
    
    redirectTo: '',
    pathMatch: 'full'
  },

  // attente path */

  { 
        path: 'view/:id',
        loadComponent: () => import('./components/dossier-etudiant/dossier-etudiant.component').then(m => m.DossierEtudiantComponent),
        data: {
            title: 'Dossier Etudiant'
        },
        children: [
          {
            path: '',
            loadChildren: () => import('./components/dossier-etudiant/routes').then((m) => m.routes)
          },
          
      ]

  },
  /* // create path

  { path: 'create',

    children:[
      {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
      },
      {
        path: '', loadComponent: () => import('./ministere.component').then(m => m.MinistereComponent) ,

      },
      {
        path: '', loadComponent: () => import('./components/create/create.component').then(m => m.CreateComponent), outlet:'secondary'
      }
    ],

  }, */


  
];
