import {Stock} from "../model/Stock";
import {getAllStocks, getPortfolioValues, postStock} from "../service/apiService";
import {useEffect, useState} from "react";
import {StockDto} from "../model/StockDto";
import {Portfolio} from "../model/Portfolio";

export default function useStocks() {

    const [stocks, setStocks] = useState<Stock[]>([])
    const [pfValues, setPfValues] = useState<Portfolio>({pfTotalReturnAbsolute: 0, pfTotalReturnPercent: 0, pfValue: 0})

    useEffect(() => {
        getAllStocks()
            .then(stock => setStocks(stock))
            .catch(console.error)
    }, [])

    useEffect(() => {
        getPortfolioValues()
            .then(pfData => setPfValues(pfData))
            .catch(console.error)
    },[stocks])

    const addStock = (newStock : StockDto) => {
        postStock(newStock)
            .then(addedStock => setStocks([...stocks, addedStock]))
            .catch(console.error)
    }

    return {stocks, addStock, pfValues}
}