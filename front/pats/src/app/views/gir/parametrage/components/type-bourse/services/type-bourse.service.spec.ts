import { TestBed } from '@angular/core/testing';

import { TypeBourseService } from './type-bourse.service';

describe('TypeBourseService', () => {
  let service: TypeBourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeBourseService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
