import { TestBed } from '@angular/core/testing';

import { TypeselectionService } from './typeselection.service';

describe('TypeselectionService', () => {
  let service: TypeselectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeselectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
