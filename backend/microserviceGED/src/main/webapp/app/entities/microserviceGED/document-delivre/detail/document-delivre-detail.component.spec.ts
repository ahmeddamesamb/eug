import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DocumentDelivreDetailComponent } from './document-delivre-detail.component';

describe('DocumentDelivre Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DocumentDelivreDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DocumentDelivreDetailComponent,
              resolve: { documentDelivre: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DocumentDelivreDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load documentDelivre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DocumentDelivreDetailComponent);

      // THEN
      expect(instance.documentDelivre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
