import { Routes } from '@angular/router';

export const routes: Routes = [

  {
    path: '',
    redirectTo: 'attente',
    pathMatch: 'full'
},

  // attente path

  { path: 'attente',

  children:[
    {
      path: '',
      redirectTo: '',
      pathMatch: 'full'
  },
    {
      path: '', loadComponent: () => import('./cycle.component').then(m => m.CycleComponent) ,

    },
    {
      path: '', loadComponent: () => import('../attente/attente.component').then(m => m.AttenteComponent), outlet:'secondary'
    }
  ],

  },
  // create path

  { path: 'create',

  children:[
    {
      path: '',
      redirectTo: '',
      pathMatch: 'full'
  },
    {
      path: '', loadComponent: () => import('./cycle.component').then(m => m.CycleComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/create/create.component').then(m => m.CreateComponent), outlet:'secondary'
    }
  ],

  },

  // update path

  { path: 'update/:id',

  children:[
    {
      path: '',
      redirectTo: '',
      pathMatch: 'full'
  },
    {
      path: '', loadComponent: () => import('./cycle.component').then(m => m.CycleComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/create/create.component').then(m => m.CreateComponent), outlet:'secondary'
    }
  ],

  },

  // update path

  { path: 'view/:id',

  children:[
    {
      path: '',
      redirectTo: '',
      pathMatch: 'full'
  },
    {
      path: '', loadComponent: () => import('./cycle.component').then(m => m.CycleComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/view/view.component').then(m => m.ViewComponent), outlet:'secondary'
    }
  ],

  },
  { path: 'associer-frais/:id',

    children:[
      {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
    },
      {
        path: '', loadComponent: () => import('./cycle.component').then(m => m.CycleComponent) ,
  
      },
      {
        path: '', loadComponent: () => import('./components/associer-frais/associer-frais.component').then(m => m.AssocierFraisComponent), outlet:'secondary'
      }
    ],
  
    }
];
