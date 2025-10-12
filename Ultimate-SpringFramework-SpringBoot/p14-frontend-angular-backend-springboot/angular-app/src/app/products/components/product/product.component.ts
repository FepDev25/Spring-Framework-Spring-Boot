import { Product } from './../../models/product';
import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { ProductFormComponent } from '../product-form/product-form.component';
import { ProductListComponent } from '../product-list/product-list.component';

@Component({
  selector: 'app-product',
  imports: [ProductFormComponent, ProductListComponent],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent implements OnInit{
  products : Product[] = [];

  productSelected : Product = {
    id: 0,
    name: '',
    description: '',
    price: 0
  }

  constructor(private service : ProductService) { }

  ngOnInit(): void {
    this.service.findAll().subscribe(products => this.products = products);
  }


  addProduct(product: Product) {
    const index = this.products.findIndex(p => p.id === product.id);

    if (index !== -1) {
      // Actualizar
      this.service.update(product).subscribe(productUpdate => {
        this.products[index] = productUpdate;
      });
    } else {
      // Eliminar id para evitar conflicto
      const { id, ...productWithoutId } = product;
      this.service.create(productWithoutId as Product).subscribe(productNew => {
        this.products.push(productNew);
      });
    }

    this.productSelected = {
      id: 0,
      name: '',
      description: '',
      price: 0
    };
  }


  loadProductUpdate(product : Product) {
    this.productSelected = {...product};
  }

  deleteProduct(id : number) {
    this.service.delete(id).subscribe(() => {
      this.products = this.products.filter(product => product.id !== id);
    })
  }

}
