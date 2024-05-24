import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeHandicapDetailComponent } from './type-handicap-detail.component';

describe('TypeHandicap Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeHandicapDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeHandicapDetailComponent,
              resolve: { typeHandicap: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeHandicapDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeHandicap on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeHandicapDetailComponent);

      // THEN
      expect(instance.typeHandicap).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
