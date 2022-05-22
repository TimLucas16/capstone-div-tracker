import {Stock} from "../model/Stock";
import "./StockCard.css";

type StockProp = {
    stock: Stock
}

export default function StockCard({stock}: StockProp) {

    return (
        <div className={"card-container"}>
            <div className={"card-details"}>
                <a className={"anker"} href={stock.website}>
                    <img className={"logo"} src={stock.image} alt={stock.companyName}/>
                </a>
                <div className={"name"}> {stock.companyName} </div>
                <div className={"shares"}> {stock.shares} </div>
                <div className={"price"}> 220,34 $ </div>
                <div className={"value"}> 1500,45 $ </div>
                <div className={"total-return"}> 121,34 $ </div>
                <div className={"allocation"}> 21,56 % </div>
            </div>
        </div>
    )
}