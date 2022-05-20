import {Stock} from "../model/Stock";

type StockProp = {
    stock : Stock
}

export default function StockCard({stock} : StockProp) {

    return (
        <div>
            {stock.name}
        </div>
    )
}