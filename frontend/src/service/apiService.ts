import axios from "axios";
import {Stock} from "../model/Stock";

export const postStock = (newStock : Stock) => {
    return axios.post("/api/stock", newStock)
        .then(response => response.data)
}