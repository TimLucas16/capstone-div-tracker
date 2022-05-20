import StockCard from "../component/StockCard";
import {Stock} from "../model/Stock";

export type StartPageProps = {
    stocks : Stock[]
}

export default function StartPage({stocks} : StartPageProps) {


    return (
            <div>
                {stocks.map(stock => <StockCard stock={stock}/>)}
            </div>
    )
}