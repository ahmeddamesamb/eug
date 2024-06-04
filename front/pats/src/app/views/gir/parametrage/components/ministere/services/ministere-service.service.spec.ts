import { TestBed } from '@angular/core/testing';

import { MinistereServiceService } from './ministere-service.service';

describe('MinistereServiceService', () => {
  let service: MinistereServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MinistereServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
