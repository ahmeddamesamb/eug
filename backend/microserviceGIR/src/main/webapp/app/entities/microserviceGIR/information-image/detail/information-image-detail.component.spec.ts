import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InformationImageDetailComponent } from './information-image-detail.component';

describe('InformationImage Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformationImageDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InformationImageDetailComponent,
              resolve: { informationImage: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InformationImageDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load informationImage on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InformationImageDetailComponent);

      // THEN
      expect(instance.informationImage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
