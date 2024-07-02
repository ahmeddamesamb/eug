import { Routes } from '@angular/router';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'gestion-formation',
        pathMatch: 'full'
    },
    {
        path: 'gestion-formation',
        children: [
          // Default component
          {
            path:"",
            loadChildren: () => import('./components/routes').then((m) => m.routes)
          }
        ]

    },
]