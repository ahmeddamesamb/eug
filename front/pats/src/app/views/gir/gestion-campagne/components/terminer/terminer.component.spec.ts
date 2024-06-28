import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerminerComponent } from './terminer.component';

describe('TerminerComponent', () => {
  let component: TerminerComponent;
  let fixture: ComponentFixture<TerminerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TerminerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TerminerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
