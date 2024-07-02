import { TestBed } from '@angular/core/testing';

import { TypeadmissionService } from './typeadmission.service';

describe('TypeadmissionService', () => {
  let service: TypeadmissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeadmissionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
