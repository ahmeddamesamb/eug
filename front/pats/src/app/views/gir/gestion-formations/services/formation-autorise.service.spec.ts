import { TestBed } from '@angular/core/testing';

import { FormationAutoriseService } from './formation-autorise.service';

describe('FormationAutoriseService', () => {
  let service: FormationAutoriseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FormationAutoriseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
