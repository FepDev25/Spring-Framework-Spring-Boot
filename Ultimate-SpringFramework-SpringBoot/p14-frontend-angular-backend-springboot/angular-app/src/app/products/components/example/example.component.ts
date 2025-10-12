import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-example',
  imports: [CommonModule],
  templateUrl: './example.component.html',
  styleUrl: './example.component.css'
})
export class ExampleComponent {

  title: string = 'Hola mundo Angular';
  enable: boolean = false;
  courses: string[] = ['Angular', 'React', 'Springboot', 'Java'];
  personas = [
    { name: 'John Doe', age: 30 },
    { name: 'Jane Smith', age: 25 },
    { name: 'Bob Johnson', age: 40 }
  ]

}
