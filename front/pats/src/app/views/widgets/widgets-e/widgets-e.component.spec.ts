import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GridModule, WidgetModule } from '@coreui/angular-pro';
import { ChartjsModule } from '@coreui/angular-chartjs';
import { IconSetService } from '@coreui/icons-angular';
import { iconSubset } from '../../../shared/icons/icon-subset';
import { WidgetsEComponent } from './widgets-e.component';

describe('WidgetsEComponent', () => {
  let component: WidgetsEComponent;
  let fixture: ComponentFixture<WidgetsEComponent>;
  let iconSetService: IconSetService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
    imports: [WidgetModule, GridModule, ChartjsModule, WidgetsEComponent],
    providers: [IconSetService]
})
    .compileComponents();
  });

  beforeEach(() => {
    iconSetService = TestBed.inject(IconSetService);
    iconSetService.icons = { ...iconSubset };

    fixture = TestBed.createComponent(WidgetsEComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
