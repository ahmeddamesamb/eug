import { Routes } from '@angular/router';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'en-cours',
        pathMatch: 'full'
    },
    {
        path: 'en-cours',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/en-cours/routes').then((m) => m.routes)
          }
        ]

    },
    {
        path: 'terminees',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/terminer/routes').then((m) => m.routes)
          }
        ]

    },
    {
        path: 'inscription',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/inscription/routes').then((m) => m.routes)
          }
        ]


    },
    {
        path: 'campagnes',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/campagnes/routes').then((m) => m.routes)
          }
        ]

    }
  ];



