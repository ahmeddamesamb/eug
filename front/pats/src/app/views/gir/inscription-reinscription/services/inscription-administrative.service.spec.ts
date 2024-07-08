import { TestBed } from '@angular/core/testing';

import { InscriptionAdministrativeService } from './inscription-administrative.service';

describe('InscriptionAdministrativeService', () => {
  let service: InscriptionAdministrativeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionAdministrativeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
