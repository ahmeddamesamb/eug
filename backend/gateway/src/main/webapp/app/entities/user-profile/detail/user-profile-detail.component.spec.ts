import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserProfileDetailComponent } from './user-profile-detail.component';

describe('UserProfile Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserProfileDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserProfileDetailComponent,
              resolve: { userProfile: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserProfileDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load userProfile on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserProfileDetailComponent);

      // THEN
      expect(instance.userProfile).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
