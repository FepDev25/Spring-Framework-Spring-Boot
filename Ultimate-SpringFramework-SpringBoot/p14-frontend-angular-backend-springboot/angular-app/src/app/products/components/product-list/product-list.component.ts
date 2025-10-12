import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Product } from '../../models/product';

@Component({
  selector: 'app-product-list',
  imports: [],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent {

  @Input() products : Product[] = [];

  @Output() onUpdateProduct = new EventEmitter<Product>();

  @Output() onDeleteProduct = new EventEmitter<number>();

  emitProductUpdate(product : Product) {
    this.onUpdateProduct.emit(product);
  }

  emitProductDelete(id : number) {
    this.onDeleteProduct.emit(id);
  }

}
