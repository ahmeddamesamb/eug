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
      path: '', loadComponent: () => import('./inscription.component').then(m => m.InscriptionComponent) ,

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
      path: '', loadComponent: () => import('./inscription.component').then(m => m.InscriptionComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/create-update/create-update.component').then(m => m.CreateUpdateComponent), outlet:'secondary'
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
      path: '', loadComponent: () => import('./inscription.component').then(m => m.InscriptionComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/create-update/create-update.component').then(m => m.CreateUpdateComponent), outlet:'secondary'
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
      path: '', loadComponent: () => import('./inscription.component').then(m => m.InscriptionComponent) ,

    },
    {
      path: '', loadComponent: () => import('./components/view/view.component').then(m => m.ViewComponent), outlet:'secondary'
    }
  ],

  }
];
