import { HttpClientJsonpModule, provideHttpClient } from '@angular/common/http';
import { importProvidersFrom } from '@angular/core';
import { Routes } from '@angular/router';

import { GoogleMapsComponent } from './google-maps.component';

export const routes: Routes = [
  {
    path: '',
    component: GoogleMapsComponent,
    data: {
      title: 'Google Maps'
    },
    providers: [
      provideHttpClient(),
      importProvidersFrom(HttpClientJsonpModule)
    ]
  }
];
