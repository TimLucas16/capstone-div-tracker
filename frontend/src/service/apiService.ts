import axios from "axios";
import {StockDto} from "../model/StockDto";
import {Stock} from "../model/Stock";

export const getAllStocks = () => {
    return axios.get("/api/stock")
        .then(response => response.data)
}

export const postStock = (newStock : StockDto) => {
    return axios.post("/api/stock", newStock)
        .then(response => response.data)
}

export const getPortfolioValues = () => {
    return axios.get("/api/stock/portfolio")
        .then(response => response.data)
}

export const putStock: (updatedstock: StockDto) => Promise<Stock> = (updatedStock: StockDto) => {
    return axios.put("/api/stock", updatedStock)
        .then(response => response.data)
}

export const getStockBy:(id : string) => Promise<Stock> = (id : string) => {
    return axios.get(`api/stock/${id}`)
        .then(response => response.data)
}