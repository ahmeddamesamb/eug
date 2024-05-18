import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeBourseDetailComponent } from './type-bourse-detail.component';

describe('TypeBourse Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeBourseDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeBourseDetailComponent,
              resolve: { typeBourse: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeBourseDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeBourse on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeBourseDetailComponent);

      // THEN
      expect(instance.typeBourse).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
