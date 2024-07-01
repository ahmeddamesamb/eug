import { Routes } from '@angular/router';
import { ParametrageComponent } from './parametrage.component';
import { noop } from 'chart.js/dist/helpers/helpers.core';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'formation',
        pathMatch: 'full'
    },
    {
        path: 'formation',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/formation/routes').then((m) => m.routes)
          }
        ]

    },
    {
        path: 'specialite',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/specialite/routes').then((m) => m.routes)
          }
        ]

    },
    {
        path: 'mention',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/mention/routes').then((m) => m.routes)
          }
        ]


    },
    {
        path: 'domaine',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/domaine/routes').then((m) => m.routes)
          }
        ]

    },
    {
        path: 'ufr',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/ufr/routes').then((m) => m.routes)
          }
        ]


    },
    {
        path: 'universite',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/universite/routes').then((m) => m.routes)
          }
        ]


    },
    {
      path: 'ministere',
      data: {
        title: 'ministere'
    },
      children: [
        // Default component
        {
          path:"",
          loadChildren: () => import('./components/ministere/routes').then((m) => m.routes)
        }
      ]
  },

  {
    path: 'cycle',
    data: {
      title: 'cycle'
  },
    children: [
      // Default component
      {
        path:"",
        loadChildren: () => import('./components/cycle/routes').then((m) => m.routes)
      }
    ]
},
    // {
    // path: 'action',
    //     loadComponent: () => import('./components/action/action.component').then(m => m.ActionComponent),
    //     children: [
    //         {
    //           path: '',
    //           loadChildren: () => import('./components/action/routes').then((m) => m.routes)
    //         },



    //     ]
    // }
  ];



