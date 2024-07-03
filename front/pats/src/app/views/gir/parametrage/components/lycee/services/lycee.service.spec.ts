import { TestBed } from '@angular/core/testing';

import { LyceeService } from './lycee.service';

describe('LyceeService', () => {
  let service: LyceeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LyceeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
