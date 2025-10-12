import axios from "axios";

let products = [
        {id:1, name: "Iphone 14", price: 1200, description: "Iphone 14 nuevo" },
        {id: 2, name: "Samsugn Galaxy S25", price: 1400, description: "Samsung Galaxy S25 nuevo" },
        {id: 4, name: "ASUS Vivobook", price: 1000, description: "ASUS Vivobook nuevo" }
];

export const listProduct = () => {
    return products;
}

const baseUrl = 'http://localhost:8080/products';

export const findAll = async() => {
    try {
        const response = await axios.get(baseUrl);
        return response;
    } catch (error) {
        console.log(error);
    }
    return null;
}

export const saveProduct = async ( {name, price, description}) =>{ 
    try {
        const response = await axios.post(baseUrl, {
            name, price, description
        });
    return response;
    } catch (error) {
        console.log(error)
    }
    return null;
}

export const updateProduct = async ( {id, name, price, description}) =>{ 
    try {
        const response = await axios.put(baseUrl + '/' + id, {
            name, price, description
        });
    return response;
    } catch (error) {
        console.log(error)
    }
    return null;
}

export const deleteProduct = async (id) =>{ 
    try {
        await axios.delete(baseUrl + '/' + id);
    } catch (error) {
        console.log(error)
    }
}   