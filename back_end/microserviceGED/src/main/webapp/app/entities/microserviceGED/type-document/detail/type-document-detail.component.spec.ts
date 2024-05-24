import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeDocumentDetailComponent } from './type-document-detail.component';

describe('TypeDocument Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeDocumentDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeDocumentDetailComponent,
              resolve: { typeDocument: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeDocumentDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeDocument on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeDocumentDetailComponent);

      // THEN
      expect(instance.typeDocument).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
