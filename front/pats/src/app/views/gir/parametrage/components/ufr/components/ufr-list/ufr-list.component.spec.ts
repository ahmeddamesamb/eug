import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UfrListComponent } from './ufr-list.component';

describe('UfrListComponent', () => {
  let component: UfrListComponent;
  let fixture: ComponentFixture<UfrListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UfrListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UfrListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
