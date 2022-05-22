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
                    <div className={"anker"}> Logo </div>
                    <div className={"name"}> Name </div>
                    <div className={"shares"}> Shares </div>
                    <div className={"price"}>Price</div>
                    <div className={"allocation"}>Allocation</div>
                    <div className={"total-return"}>Total Return</div>
                </div>
            </div>

            <div> {stocks.map(stock => <StockCard stock={stock}/>)} </div>
        </div>
    )
}