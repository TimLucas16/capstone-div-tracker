import StockCard from "../component/StockCard";
import {Stock} from "../model/Stock";

export type StartPageProps = {
    stocks : Stock[]
}

export default function StartPage({stocks} : StartPageProps) {


    return (
        <div>
            <div className={"card-container"}>
                <div className={"card-legend"}>
                    <div className={"logo-legend"}> Logo </div>
                    <div className={"name-legend"}> Name </div>
                    <div className={"share-legend"}> shares </div>
                </div>
            </div>

            <div> {stocks.map(stock => <StockCard stock={stock}/>)} </div>
        </div>
    )
}