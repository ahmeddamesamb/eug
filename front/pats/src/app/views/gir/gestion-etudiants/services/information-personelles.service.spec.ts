import { TestBed } from '@angular/core/testing';

import { InformationPersonellesService } from './information-personelles.service';

describe('InformationPersonellesService', () => {
  let service: InformationPersonellesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InformationPersonellesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
