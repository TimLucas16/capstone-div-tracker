import {Stock} from "../model/Stock";
import {postStock} from "../service/apiService";

export default function useStocks() {

    const addStock = (newStock : Stock) => {
        postStock(newStock)
            .catch(console.error)
    }

    return {addStock}
}