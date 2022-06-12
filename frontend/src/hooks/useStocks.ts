import {Stock} from "../model/Stock";
import {
    getAllStocks,
    getPortfolioValues,
    getStockBy,
    postStock,
    putStock,
    stockSearch
} from "../service/apiService";
import {useEffect, useState} from "react";
import {StockDto} from "../model/StockDto";
import {Portfolio} from "../model/Portfolio";
import {SearchStock} from "../model/SearchStock";
import toast from 'react-hot-toast';

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
        totalReturnPercent: 0,
        image: "",
        website: "",
        isin: ""
    })
    const [stockList, setStockList] = useState<SearchStock[]>([])

    const addStock = (newStock: StockDto) => {
        postStock(newStock)
            .then(addedStock => {setStocks([...stocks, addedStock])})
            .then(() => toast.success(`Stock with Symbol ${newStock.symbol} added`))
            .catch(() => toast.error(`Adding Stock with Symbol: ${newStock.symbol} failed`))
    }

    useEffect(() => {
        getAllStocks()
            .then(allStocks => setStocks(allStocks))
            .catch(() => toast.error(`Connection failed. Please try again!`))
    }, [])

    useEffect(() => {
        getPortfolioValues()
            .then(pfData => setPfValues(pfData))
            .catch(() => toast.error(`Connection failed. Please try again!`))
    }, [stocks])

    const updateStock = (updatedStock: StockDto) => {
        return putStock(updatedStock)
            .then(changedStock => {
                if (!changedStock) {
                    setStocks(stocks.filter((item: Stock) => (item.symbol !== updatedStock.symbol)))
                    toast.success("Stock deleted")
                } else {
                    setStocks(stocks.map((item: Stock) => (item.symbol === changedStock.symbol ? changedStock : item)))
                    toast.success("Stock updated")
                }
            })
            .catch(() => toast.error(`Updating Stock with Symbol: ${updatedStock.symbol} failed`))
    }

    const getStockById = (id: string) => {
        return getStockBy(id)
            .then(data => setStock(data))
            .catch(() => toast.error(`Connection failed. Please try again!`))
    }

    const searchForStock = (company: string) => {
        return stockSearch(company)
            .then(allStocks => {
                if(allStocks.length === 0) {
                toast("search was without result")
                } else {
                    setStockList(allStocks)
                }
            })
            .catch(() => toast.error(`Connection failed. Please try again!`))
    }

    return {stocks, addStock, pfValues, updateStock, stock, getStockById, searchForStock, stockList}
}