import {Stock} from "../model/Stock";

type StockProp = {
    stock: Stock
}

export default function StockCard({stock}: StockProp) {

    return (
        <div>
            <img src={stock.image} alt={stock.companyName}/>
            <div>
                {stock.companyName}
            </div>
        </div>
    )
}