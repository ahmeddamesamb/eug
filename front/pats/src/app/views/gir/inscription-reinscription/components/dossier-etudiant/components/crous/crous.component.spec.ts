import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CrousComponent } from './crous.component';

describe('CrousComponent', () => {
  let component: CrousComponent;
  let fixture: ComponentFixture<CrousComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CrousComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CrousComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
