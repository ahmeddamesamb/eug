import { TestBed } from '@angular/core/testing';

import { MentionServiceService } from './mention-service.service';

describe('MentionServiceService', () => {
  let service: MentionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MentionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
