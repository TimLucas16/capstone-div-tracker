import {SearchStock} from "../model/SearchStock";
import {FormEvent} from "react";

type SearchCardProps = {
    stock: SearchStock
    selectStock: (symbol: string) => void
 }

export default function SearchCard({stock, selectStock}: SearchCardProps) {

    const selected = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()

        selectStock(stock.symbol)
    }

    return (
        <div>
            <form onSubmit={selected}>
                <div>{stock.name}</div>
                <div>{stock.symbol}</div>
                <button type={"submit"}>select</button>
            </form>
        </div>
    )
}