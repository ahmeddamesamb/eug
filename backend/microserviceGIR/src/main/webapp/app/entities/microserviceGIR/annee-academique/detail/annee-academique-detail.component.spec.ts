import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnneeAcademiqueDetailComponent } from './annee-academique-detail.component';

describe('AnneeAcademique Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnneeAcademiqueDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AnneeAcademiqueDetailComponent,
              resolve: { anneeAcademique: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnneeAcademiqueDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load anneeAcademique on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnneeAcademiqueDetailComponent);

      // THEN
      expect(instance.anneeAcademique).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
