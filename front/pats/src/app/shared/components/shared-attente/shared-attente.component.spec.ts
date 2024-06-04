import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SharedAttenteComponent } from './shared-attente.component';

describe('SharedAttenteComponent', () => {
  let component: SharedAttenteComponent;
  let fixture: ComponentFixture<SharedAttenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedAttenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SharedAttenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
