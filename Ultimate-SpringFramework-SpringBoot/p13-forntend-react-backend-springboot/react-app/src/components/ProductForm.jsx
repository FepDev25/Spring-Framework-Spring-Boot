import { useEffect, useState } from "react";


const initialForm = {
    id: 0,
    name: '',
    price: 0,
    description: ''
}

export const ProductForm = ( {handlerAdd, productSelected} ) => {

    const [form, setForm] = useState(initialForm);

    const {id, name, price, description} = form;

    useEffect( () => {
        setForm(productSelected);
    }, [productSelected] ); 
    
    return(
        <>
            <form onSubmit={ (e) => {
                e.preventDefault();

                if (!name || !price || !description) {
                    alert('Todos los campos son obligatorios');
                    return;
                };

                handlerAdd(form);
                setForm(initialForm);
            }}>
                <input className="form-control my-3 w-75" type="text" name="name" placeholder="Name" value={name} onChange={ 
                    (e) => setForm({...form, name: e.target.value}) 
                } />

                <input className="form-control my-3 w-75" type="number" name="price" placeholder="Price" value={price} onChange={
                    (e) => setForm({...form, price: e.target.value}) 
                } />

                <input className="form-control my-3 w-75" type="text" name="description" placeholder="Description" value={description} onChange={
                    (e) => setForm({...form, description: e.target.value}) 
                } />

                <button className="btn btn-primary" type="submit">
                    {id !== 0 ? 'Modificar' : 'Guardar'}
                </button>
            </form>

            
        </>
    )
}