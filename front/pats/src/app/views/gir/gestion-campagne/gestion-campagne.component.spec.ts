import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionCampagneComponent } from './gestion-campagne.component';

describe('GestionCampagneComponent', () => {
  let component: GestionCampagneComponent;
  let fixture: ComponentFixture<GestionCampagneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionCampagneComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GestionCampagneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
