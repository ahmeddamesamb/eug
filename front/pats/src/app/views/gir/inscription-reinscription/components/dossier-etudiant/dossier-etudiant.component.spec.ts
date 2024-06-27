import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DossierEtudiantComponent } from './dossier-etudiant.component';

describe('DossierEtudiantComponent', () => {
  let component: DossierEtudiantComponent;
  let fixture: ComponentFixture<DossierEtudiantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DossierEtudiantComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DossierEtudiantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
