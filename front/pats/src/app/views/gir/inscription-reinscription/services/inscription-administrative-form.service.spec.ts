import { TestBed } from '@angular/core/testing';

import { InscriptionAdministrativeFormService } from './inscription-administrative-form.service';

describe('InscriptionAdministrativeFormService', () => {
  let service: InscriptionAdministrativeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionAdministrativeFormService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
