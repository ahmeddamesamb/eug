import { Routes } from '@angular/router';
import { GestionEtudiantsComponent } from './gestion-etudiants.component';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'gestion-etudiant',
        pathMatch: 'full'
    },
    /* {      
        path: 'create',
        loadComponent: () => import('./create/create.component').then(m => m.CreateComponent),
        
    },
    {      
        path: 'update/:id',
        loadComponent: () => import('./create/create.component').then(m => m.CreateComponent),
        
    }, */
  ];



