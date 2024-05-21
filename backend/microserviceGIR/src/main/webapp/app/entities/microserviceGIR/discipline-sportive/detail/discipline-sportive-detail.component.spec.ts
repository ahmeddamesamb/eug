import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplineSportiveDetailComponent } from './discipline-sportive-detail.component';

describe('DisciplineSportive Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisciplineSportiveDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DisciplineSportiveDetailComponent,
              resolve: { disciplineSportive: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DisciplineSportiveDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load disciplineSportive on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisciplineSportiveDetailComponent);

      // THEN
      expect(instance.disciplineSportive).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
