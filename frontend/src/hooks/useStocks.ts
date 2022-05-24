import {Stock} from "../model/Stock";
import {getAllStocks, postStock} from "../service/apiService";
import {useEffect, useState} from "react";
import {StockDto} from "../model/StockDto";

export default function useStocks() {

    const [stocks, setStocks] = useState<Stock[]>([])

    useEffect(() => {
        getAllStocks()
            .then(stock => setStocks(stock))
            .catch(console.error)
    }, [])

    const addStock = (newStock : StockDto) => {
        postStock(newStock)
            .then(addedStock => setStocks([...stocks, addedStock]))
            .catch(console.error)
    }

    return {stocks, addStock}
}