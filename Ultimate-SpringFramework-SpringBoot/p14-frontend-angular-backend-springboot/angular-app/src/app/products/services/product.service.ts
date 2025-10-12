import { map, Observable, of } from 'rxjs';
import { Product } from './../models/product';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private url = 'http://localhost:8080/products';

  private products: Product[] = [
    { id: 1, name: 'Iphone 17', description: 'Iphone 17 nuevo', price: 1800 },
    { id: 2, name: 'Samsung Galaxy S22', description: 'Samsung Galaxy S22 Negro', price: 900 },
    { id: 3, name: 'PS5', description: 'Playstation 5', price: 700 }
  ]

  constructor(private http : HttpClient) { }

  findAll() : Observable<Product[]> {
    return this.http.get<Product[]>(this.url).pipe(
      map((response : any) => response._embedded.products as Product[])
    );
  }

  create(product : Product) : Observable<Product> {
    console.log(product);
    return this.http.post<Product>(this.url, product);
  }

  update(product : Product) : Observable<Product> {
    return this.http.put<Product>(`${this.url}/${product.id}`, product);
  }

  delete(id : number) : Observable<any> {
    return this.http.delete<any>(`${this.url}/${id}`);
  }
}
