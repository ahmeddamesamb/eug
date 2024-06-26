import { TestBed } from '@angular/core/testing';

import { UfrServiceService } from './ufr-service.service';

describe('UfrServiceService', () => {
  let service: UfrServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UfrServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
