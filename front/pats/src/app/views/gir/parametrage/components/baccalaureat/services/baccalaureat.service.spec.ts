import { TestBed } from '@angular/core/testing';

import { BaccalaureatService } from './baccalaureat.service';

describe('BaccalaureatService', () => {
  let service: BaccalaureatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaccalaureatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
