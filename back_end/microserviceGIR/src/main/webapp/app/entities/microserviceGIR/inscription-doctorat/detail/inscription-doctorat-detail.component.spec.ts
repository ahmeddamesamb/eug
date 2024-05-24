import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InscriptionDoctoratDetailComponent } from './inscription-doctorat-detail.component';

describe('InscriptionDoctorat Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionDoctoratDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InscriptionDoctoratDetailComponent,
              resolve: { inscriptionDoctorat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InscriptionDoctoratDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load inscriptionDoctorat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InscriptionDoctoratDetailComponent);

      // THEN
      expect(instance.inscriptionDoctorat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
