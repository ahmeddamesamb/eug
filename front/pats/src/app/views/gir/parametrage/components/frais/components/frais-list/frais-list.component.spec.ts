import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FraisListComponent } from './frais-list.component';

describe('FraisListComponent', () => {
  let component: FraisListComponent;
  let fixture: ComponentFixture<FraisListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FraisListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FraisListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
