import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ServiceUserDetailComponent } from './service-user-detail.component';

describe('ServiceUser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceUserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ServiceUserDetailComponent,
              resolve: { serviceUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ServiceUserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load serviceUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ServiceUserDetailComponent);

      // THEN
      expect(instance.serviceUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
