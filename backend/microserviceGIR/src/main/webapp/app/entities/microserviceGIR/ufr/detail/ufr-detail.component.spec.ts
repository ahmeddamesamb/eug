import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UFRDetailComponent } from './ufr-detail.component';

describe('UFR Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UFRDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UFRDetailComponent,
              resolve: { uFR: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UFRDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load uFR on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UFRDetailComponent);

      // THEN
      expect(instance.uFR).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
