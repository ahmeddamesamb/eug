import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaiementFormationPriveeDetailComponent } from './paiement-formation-privee-detail.component';

describe('PaiementFormationPrivee Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaiementFormationPriveeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaiementFormationPriveeDetailComponent,
              resolve: { paiementFormationPrivee: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaiementFormationPriveeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load paiementFormationPrivee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaiementFormationPriveeDetailComponent);

      // THEN
      expect(instance.paiementFormationPrivee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
