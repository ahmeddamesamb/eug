import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationAutoriseeDetailComponent } from './formation-autorisee-detail.component';

describe('FormationAutorisee Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationAutoriseeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormationAutoriseeDetailComponent,
              resolve: { formationAutorisee: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormationAutoriseeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formationAutorisee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormationAutoriseeDetailComponent);

      // THEN
      expect(instance.formationAutorisee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
