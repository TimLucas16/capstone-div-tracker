import {Stock} from "../model/Stock";
import {getAllStocks, postStock} from "../service/apiService";
import {useEffect, useState} from "react";

export default function useStocks() {

    const [stocks, setStocks] = useState<Stock[]>([])

    useEffect(() => {
        getAllStocks()
            .then(stock => setStocks(stock))
            .catch(console.error)
    }, [])

    const addStock = (newStock : Stock) => {
        postStock(newStock)
            .then(addedStock => setStocks([...stocks, addedStock]))
            .catch(console.error)
    }

    return {stocks, addStock}
}