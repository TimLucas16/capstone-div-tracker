import axios from "axios";
import {StockDto} from "../model/StockDto";

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

export const putStock = (updatedStock: StockDto) => {
    return axios.put("/api/stock", updatedStock)
        .then(response => response.data)
}