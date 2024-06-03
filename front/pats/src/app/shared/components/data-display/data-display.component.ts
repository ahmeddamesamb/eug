import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-data-display',
  standalone: true,
  imports: [],
  templateUrl: './data-display.component.html',
  styleUrl: './data-display.component.scss'
})
export class DataDisplayComponent {


  @Input() public label: string | undefined ="";

  @Input() public data: string | undefined ="";

  @Input() public iclass: string | undefined ="";

  customClass = "text-"+this.iclass +" border-"+this.iclass;








}
