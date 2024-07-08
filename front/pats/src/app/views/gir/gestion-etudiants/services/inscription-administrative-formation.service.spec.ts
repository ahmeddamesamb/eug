import { TestBed } from '@angular/core/testing';

import { InscriptionAdministrativeFormationService } from './inscription-administrative-formation.service';

describe('InscriptionAdministrativeFormationService', () => {
  let service: InscriptionAdministrativeFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionAdministrativeFormationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
