import { Routes } from '@angular/router';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'inscription-reinscription',
        pathMatch: 'full'
    },
    {      
        path: 'inscription-reinscription',
        loadComponent: () => import('./components/inscription/inscription.component').then(m => m.InscriptionComponent),
        
    },
    {      
        path: 'bibliotheque',
        loadComponent: () => import('./components//bibliotheque/bibliotheque.component').then(m => m.BibliothequeComponent),
        
    },
    {      
        path: 'acp',
        loadComponent: () => import('./components/acp/acp.component').then(m => m.AcpComponent),
        
    },
    {      
        path: 'crous',
        loadComponent: () => import('./components/crous/crous.component').then(m => m.CrousComponent),
        
    },
    {      
        path: 'infos-etudiant',
        loadComponent: () => import('./components/infos-etudiant/infos-etudiant.component').then(m => m.InfosEtudiantComponent),
        
    },
    
  ];



