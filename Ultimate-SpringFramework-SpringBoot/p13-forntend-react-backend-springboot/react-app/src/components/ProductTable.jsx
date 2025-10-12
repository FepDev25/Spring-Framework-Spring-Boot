import PropTypes from "prop-types"
import { ProductDetail } from "./ProductDetail"

export const ProductTable = ( {products, handlerRemove, handlerProductSelected} ) => {
    return(
        <>
            <table className="table table-hover">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Price</th>
                        <th>Description</th>
                        <th>Eliminar</th>
                        <th>Modificar</th>
                    </tr>
                </thead>

                <tbody>
                    {
                        products.map((product) =>(
                            <ProductDetail product={product} key={product.name} handlerRemove={handlerRemove} handlerProductSelected={handlerProductSelected} />
                        ))
                    }
                </tbody>
            </table>

        </>
    )
}

ProductTable.propTypes = {
    products: PropTypes.array.isRequired
}