import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product-form',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent {

  @Input() product : Product = {
    id: 0,
    name: '',
    description: '',
    price: 0
  }

  @Output() newProductEvent = new EventEmitter<Product>();

  save (productForm : NgForm) {
    (productForm.valid) ? this.newProductEvent.emit(this.product) : null;
    productForm.reset();
  }

  clean () {
    this.product = {
      id: 0,
      name: '',
      description: '',
      price: 0
    }
  }

}
