import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EtapeInscriptionComponent } from './etape-inscription.component';

describe('EtapeInscriptionComponent', () => {
  let component: EtapeInscriptionComponent;
  let fixture: ComponentFixture<EtapeInscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EtapeInscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EtapeInscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
