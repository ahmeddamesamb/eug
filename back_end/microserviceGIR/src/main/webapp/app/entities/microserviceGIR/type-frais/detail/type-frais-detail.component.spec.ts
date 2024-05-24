import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeFraisDetailComponent } from './type-frais-detail.component';

describe('TypeFrais Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeFraisDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeFraisDetailComponent,
              resolve: { typeFrais: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeFraisDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeFrais on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeFraisDetailComponent);

      // THEN
      expect(instance.typeFrais).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
