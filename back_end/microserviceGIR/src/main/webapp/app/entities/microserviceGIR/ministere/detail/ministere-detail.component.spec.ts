import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MinistereDetailComponent } from './ministere-detail.component';

describe('Ministere Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MinistereDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MinistereDetailComponent,
              resolve: { ministere: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MinistereDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ministere on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MinistereDetailComponent);

      // THEN
      expect(instance.ministere).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
