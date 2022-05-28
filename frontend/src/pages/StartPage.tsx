import StockCard from "../component/StockCard";
import {Stock} from "../model/Stock";
import {Portfolio} from "../model/Portfolio";

export type StartPageProps = {
    stocks : Stock[]
    pfValues : Portfolio
}

export default function StartPage({stocks, pfValues} : StartPageProps) {


    return (
        <div>
            <div>{(pfValues.pfValue / 100).toFixed(2)} $</div>
            <div>{(pfValues.pfTotalReturnAbsolute / 100).toFixed(2)} $</div>
            <div>{pfValues.pfTotalReturnPercent} %</div>
            <div className={"card-container"}>
                <div className={"card-legend"}>
                    <div className={"anker"}> Logo </div>
                    <div className={"name"}> Name </div>
                    <div className={"shares"}> Shares </div>
                    <div className={"price"}> Price </div>
                    <div className={"value"}> Value </div>
                    <div className={"total-return"}> Total Return </div>
                    <div className={"allocation"}> Allocation </div>
                </div>
            </div>
            <div> {stocks.map(stock => <StockCard key={stock.symbol} stock={stock}/>)} </div>
        </div>
    )
}