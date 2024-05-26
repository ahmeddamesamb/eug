import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CampagneDetailComponent } from './campagne-detail.component';

describe('Campagne Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CampagneDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CampagneDetailComponent,
              resolve: { campagne: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CampagneDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load campagne on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CampagneDetailComponent);

      // THEN
      expect(instance.campagne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
