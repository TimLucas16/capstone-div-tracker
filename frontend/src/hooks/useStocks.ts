import {Stock} from "../model/Stock";
import {getAllStocks, getPortfolioValues, getStockBy, postStock, putStock} from "../service/apiService";
import {useEffect, useState} from "react";
import {StockDto} from "../model/StockDto";
import {Portfolio} from "../model/Portfolio";
import {stockSearch} from "../service/externalApiService";
import {SearchStock} from "../model/SearchStock";

export default function useStocks() {

    const [stocks, setStocks] = useState<Stock[]>([])
    const [pfValues, setPfValues] = useState<Portfolio>({pfTotalReturnAbsolute: 0, pfTotalReturnPercent: 0, pfValue: 0})
    const [stock, setStock] = useState<Stock>({
        id: "",
        symbol: "",
        companyName: "",
        shares: 0,
        price: 0,
        value: 0,
        totalReturn: 0,
        image: "",
        website: ""
    })
    const [stockList, setStockList] = useState<SearchStock[]>([])
    const [selectedStock, setSelectedStock] = useState<SearchStock>({symbol: "", name: ""})

    const addStock = (newStock: StockDto) => {
        postStock(newStock)
            .then(addedStock => setStocks([...stocks, addedStock]))
            .catch(console.error)
    }

    useEffect(() => {
        getAllStocks()
            .then(allStocks => setStocks(allStocks))
            .catch(console.error)
    }, [])

    useEffect(() => {
        getPortfolioValues()
            .then(pfData => setPfValues(pfData))
            .catch(console.error)
    }, [stocks])

    const updateStock = (updatedStock: StockDto) => {
        return putStock(updatedStock)
            .then(changedStock => {
                if (!changedStock) {
                    setStocks(stocks.filter((item: Stock) => (item.symbol !== updatedStock.symbol)))
                } else {
                    setStocks(stocks.map((item: Stock) => (item.symbol === changedStock.symbol ? changedStock : item)))
                }
            })
            .catch(console.error)
    }

    const getStockById = (id: string) => {
        return getStockBy(id)
            .then(data => setStock(data))
            .catch(console.error)
    }

    const searchForStock = (company : string) => {
        return stockSearch(company)
        .then(allStocks => setStockList(allStocks))
            .catch(console.error)
    }

    const selectStock = (stock: SearchStock) => {
        setSelectedStock(stock)
    }

    return {stocks, addStock, pfValues, updateStock, stock, getStockById, searchForStock, stockList, selectedStock, selectStock}
}