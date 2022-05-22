import {Stock} from "../model/Stock";
import "./StockCard.css";

type StockProp = {
    stock: Stock
}

export default function StockCard({stock}: StockProp) {

    return (
        <div className={"card-container"}>
            <div className={"card-details"}>
                <a href={stock.website}><img className={"logo"} src={stock.image} alt={stock.companyName}/></a>
                <div className={"name"}> {stock.companyName} </div>
                <div className={"shares"}> {stock.shares} </div>
            </div>
        </div>
    )
}