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