import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlanningDetailComponent } from './planning-detail.component';

describe('Planning Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlanningDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlanningDetailComponent,
              resolve: { planning: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlanningDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load planning on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlanningDetailComponent);

      // THEN
      expect(instance.planning).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
