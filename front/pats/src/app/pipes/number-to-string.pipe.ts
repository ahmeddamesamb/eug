import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberToString',
  standalone: true
})
export class NumberToStringPipe implements PipeTransform {

  transform(value: number, ...args: unknown[]): string {
    if (value==1) {
      return "en cours";
      
    } else {
      return "-----";
      
    }
  }

}
