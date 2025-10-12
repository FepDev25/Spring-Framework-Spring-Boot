import { useEffect, useState } from "react"
import { deleteProduct, findAll, saveProduct, updateProduct } from "../services/ProductService";
import { ProductTable } from "./ProductTable";
import { ProductForm } from "./ProductForm";


export const ProductApp = () =>{

    const  [products, setProducts] = useState([]);

    const [productSelected, setProductSelected] = useState({
        id: 0,
        name: '',
        price: 0,
        description: ''
    });

    const getProducts = async() => {
        const initProducts = await findAll();
        console.log(initProducts);
        setProducts(initProducts.data._embedded.products);
    }

    useEffect(() => {
        getProducts();
    }, []);

    const handlerAddProduct = async (productNew) => {
        console.log(productNew);

        if (productNew.id !== 0) {
            const response = await updateProduct(productNew);

            getProducts();

            setProducts(products.map( productExistente => {
            if (productExistente.id === response.id) {
                return {...response.data};
            }
            return productExistente;
        }));
        } else {
            const response = await saveProduct(productNew);

            getProducts();

            setProducts([...products, {...response.data}]);
        }
    }

    const handlerRemoveProduct =  async (id) => {
        console.log(id);

        await deleteProduct(id);

        setProducts(products.filter(product => product.id != id));
    }

    const handlerProductSelected = (product) => {
        console.log(product);
        setProductSelected({...product});
    }

    return(
        <>
            <div  className="container my-4" >
                <h1>Productos</h1>

                <div className="row">   

                    <div className="col">
                        <ProductForm handlerAdd = {handlerAddProduct} productSelected = {productSelected} />
                    </div>

                    <div className="col"> 
                        {
                            products.length > 0 ? <ProductTable products={products} handlerRemove = {handlerRemoveProduct} handlerProductSelected = {handlerProductSelected} />
                                : <p className="alert alert-warning">No hay productos</p>
                        }
                        
                    </div>

                </div>      
                
            </div>
            
        
        </>
    )
}