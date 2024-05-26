import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ZoneDetailComponent } from './zone-detail.component';

describe('Zone Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ZoneDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ZoneDetailComponent,
              resolve: { zone: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ZoneDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load zone on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ZoneDetailComponent);

      // THEN
      expect(instance.zone).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
