import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeAdmissionDetailComponent } from './type-admission-detail.component';

describe('TypeAdmission Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeAdmissionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeAdmissionDetailComponent,
              resolve: { typeAdmission: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeAdmissionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeAdmission on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeAdmissionDetailComponent);

      // THEN
      expect(instance.typeAdmission).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
