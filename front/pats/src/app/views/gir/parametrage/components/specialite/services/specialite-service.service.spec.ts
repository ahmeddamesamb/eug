import { TestBed } from '@angular/core/testing';

import { SpecialiteServiceService } from './specialite-service.service';

describe('SpecialiteServiceService', () => {
  let service: SpecialiteServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SpecialiteServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
