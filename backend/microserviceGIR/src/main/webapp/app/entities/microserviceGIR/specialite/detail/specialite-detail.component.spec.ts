import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SpecialiteDetailComponent } from './specialite-detail.component';

describe('Specialite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SpecialiteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SpecialiteDetailComponent,
              resolve: { specialite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SpecialiteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load specialite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SpecialiteDetailComponent);

      // THEN
      expect(instance.specialite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
