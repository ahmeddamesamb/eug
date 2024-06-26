import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { HistoriqueConnexionDetailComponent } from './historique-connexion-detail.component';

describe('HistoriqueConnexion Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoriqueConnexionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: HistoriqueConnexionDetailComponent,
              resolve: { historiqueConnexion: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HistoriqueConnexionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load historiqueConnexion on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoriqueConnexionDetailComponent);

      // THEN
      expect(instance.historiqueConnexion).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
