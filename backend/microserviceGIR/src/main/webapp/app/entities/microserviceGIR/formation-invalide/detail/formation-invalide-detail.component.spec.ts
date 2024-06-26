import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationInvalideDetailComponent } from './formation-invalide-detail.component';

describe('FormationInvalide Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationInvalideDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormationInvalideDetailComponent,
              resolve: { formationInvalide: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormationInvalideDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formationInvalide on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormationInvalideDetailComponent);

      // THEN
      expect(instance.formationInvalide).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
