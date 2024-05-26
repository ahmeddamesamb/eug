import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationPriveeDetailComponent } from './formation-privee-detail.component';

describe('FormationPrivee Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationPriveeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormationPriveeDetailComponent,
              resolve: { formationPrivee: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormationPriveeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formationPrivee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormationPriveeDetailComponent);

      // THEN
      expect(instance.formationPrivee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
