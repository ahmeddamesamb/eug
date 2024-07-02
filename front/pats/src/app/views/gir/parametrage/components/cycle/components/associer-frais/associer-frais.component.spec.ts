import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssocierFraisComponent } from './associer-frais.component';

describe('AssocierFraisComponent', () => {
  let component: AssocierFraisComponent;
  let fixture: ComponentFixture<AssocierFraisComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AssocierFraisComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AssocierFraisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
