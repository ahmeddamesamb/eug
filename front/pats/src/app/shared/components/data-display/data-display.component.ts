import { Component, Input } from '@angular/core';



import { NumberToStringPipe } from '../../../pipes/number-to-string.pipe'

@Component({
    selector: 'app-data-display',
    standalone: true,
    templateUrl: './data-display.component.html',
    styleUrl: './data-display.component.scss',
    imports: [NumberToStringPipe]
})
export class DataDisplayComponent {


  @Input() public label: string | undefined ="";

  @Input() public data: string | undefined ="";

  @Input() public iclass: string | undefined ="";

  customClass = "text-"+this.iclass +" border-"+this.iclass;








}
