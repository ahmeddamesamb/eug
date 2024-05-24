import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaiementFraisDetailComponent } from './paiement-frais-detail.component';

describe('PaiementFrais Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaiementFraisDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaiementFraisDetailComponent,
              resolve: { paiementFrais: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaiementFraisDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load paiementFrais on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaiementFraisDetailComponent);

      // THEN
      expect(instance.paiementFrais).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
