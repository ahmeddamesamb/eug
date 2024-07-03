import { TestBed } from '@angular/core/testing';

import { InformationPersonnellesService } from './information-personnelles.service';

describe('InformationPersonnellesService', () => {
  let service: InformationPersonnellesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InformationPersonnellesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
