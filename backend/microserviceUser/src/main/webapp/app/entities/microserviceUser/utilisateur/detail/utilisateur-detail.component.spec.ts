import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UtilisateurDetailComponent } from './utilisateur-detail.component';

describe('Utilisateur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UtilisateurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UtilisateurDetailComponent,
              resolve: { utilisateur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UtilisateurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load utilisateur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UtilisateurDetailComponent);

      // THEN
      expect(instance.utilisateur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
