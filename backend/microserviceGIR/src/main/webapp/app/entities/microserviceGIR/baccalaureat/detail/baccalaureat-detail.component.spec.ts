import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BaccalaureatDetailComponent } from './baccalaureat-detail.component';

describe('Baccalaureat Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaccalaureatDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BaccalaureatDetailComponent,
              resolve: { baccalaureat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BaccalaureatDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load baccalaureat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BaccalaureatDetailComponent);

      // THEN
      expect(instance.baccalaureat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
