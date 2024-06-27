import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcpComponent } from './acp.component';

describe('AcpComponent', () => {
  let component: AcpComponent;
  let fixture: ComponentFixture<AcpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AcpComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AcpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
