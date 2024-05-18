import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationValideDetailComponent } from './formation-valide-detail.component';

describe('FormationValide Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationValideDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormationValideDetailComponent,
              resolve: { formationValide: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormationValideDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formationValide on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormationValideDetailComponent);

      // THEN
      expect(instance.formationValide).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
