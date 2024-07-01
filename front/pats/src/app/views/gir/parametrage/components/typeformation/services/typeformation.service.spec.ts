import { TestBed } from '@angular/core/testing';

import { TypeformationService } from './typeformation.service';

describe('TypeformationService', () => {
  let service: TypeformationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeformationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
