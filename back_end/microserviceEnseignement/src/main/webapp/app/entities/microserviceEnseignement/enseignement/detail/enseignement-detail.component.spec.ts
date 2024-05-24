import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnseignementDetailComponent } from './enseignement-detail.component';

describe('Enseignement Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnseignementDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EnseignementDetailComponent,
              resolve: { enseignement: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnseignementDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load enseignement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnseignementDetailComponent);

      // THEN
      expect(instance.enseignement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
