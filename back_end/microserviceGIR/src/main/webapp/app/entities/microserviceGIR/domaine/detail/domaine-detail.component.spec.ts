import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DomaineDetailComponent } from './domaine-detail.component';

describe('Domaine Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DomaineDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DomaineDetailComponent,
              resolve: { domaine: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DomaineDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load domaine on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DomaineDetailComponent);

      // THEN
      expect(instance.domaine).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
