import { TestBed } from '@angular/core/testing';

import { TypeHandicapService } from './type-handicap.service';

describe('TypeHandicapService', () => {
  let service: TypeHandicapService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeHandicapService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
