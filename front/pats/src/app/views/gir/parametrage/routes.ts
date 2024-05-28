import { Routes } from '@angular/router';
import { ParametrageComponent } from './parametrage.component';
export const routes: Routes = [

    {
        path: '',
        redirectTo: 'formation',
        pathMatch: 'full'
    },
    {
        path: 'formation',
        loadComponent: () => import('./components/formation/formation.component').then(m => m.FormationComponent),
    },
    {
        path: 'specialite',
        loadComponent: () => import('./components/specialite/specialite.component').then(m => m.SpecialiteComponent),
    },
    {
        path: 'mention',
        loadComponent: () => import('./components/mention/mention.component').then(m => m.MentionComponent),
    },
    {
        path: 'domaine',
        loadComponent: () => import('./components/domaine/domaine.component').then(m => m.DomaineComponent),
    },
    {
        path: 'ufr',
        loadComponent: () => import('./components/ufr/ufr.component').then(m => m.UfrComponent),
    },
    {
        path: 'universite',
        loadComponent: () => import('./components/universite/universite.component').then(m => m.UniversiteComponent),
    },
    {
        path: 'ministere',
        loadComponent: () => import('./components/ministere/ministere.component').then(m => m.MinistereComponent),
    },
  ];



