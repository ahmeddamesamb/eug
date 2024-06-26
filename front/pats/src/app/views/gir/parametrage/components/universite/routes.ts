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
      path: '', loadComponent: () => import('./universite.component').then(m => m.UniversiteComponent) ,

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
      path: '', loadComponent: () => import('./universite.component').then(m => m.UniversiteComponent) ,

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
      path: '', loadComponent: () => import('./universite.component').then(m => m.UniversiteComponent) ,

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
      path: '', loadComponent: () => import('./universite.component').then(m => m.UniversiteComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/view/view.component').then(m => m.ViewComponent), outlet:'secondary'
    }
  ],

  }
];
