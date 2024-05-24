import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplineSportiveEtudiantDetailComponent } from './discipline-sportive-etudiant-detail.component';

describe('DisciplineSportiveEtudiant Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisciplineSportiveEtudiantDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DisciplineSportiveEtudiantDetailComponent,
              resolve: { disciplineSportiveEtudiant: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DisciplineSportiveEtudiantDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load disciplineSportiveEtudiant on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisciplineSportiveEtudiantDetailComponent);

      // THEN
      expect(instance.disciplineSportiveEtudiant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
