import { Routes } from '@angular/router';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'attente',
        pathMatch: 'full'
    },
     {      
        path: 'view',
        loadComponent: () => import('./view/view.component').then(m => m.ViewComponent),
        
    },
    {      
        //path: 'update/:id',
        path: 'formation-autorise',
        loadComponent: () => import('./formation-add-update/formation-add-update.component').then(m => m.FormationAddUpdateComponent),
        
    },
    {      
        path: 'attente',
        loadComponent: () => import('./attente/attente.component').then(m => m.AttenteComponent),
        
    },
  ];



