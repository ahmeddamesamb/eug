import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UfrComponent } from './ufr.component';

describe('UfrComponent', () => {
  let component: UfrComponent;
  let fixture: ComponentFixture<UfrComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UfrComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UfrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
