import {SearchStock} from "../model/SearchStock";
import {FormEvent} from "react";

type SearchCardProps = {
    stock: SearchStock
    selectStock: () => void
}

export default function SearchCard({stock, selectStock}: SearchCardProps) {

    const selected = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        selectStock()
    }

    return (
        <div className={"search-container"}>
            <form className={"seachCard-form"} onSubmit={selected}>
                <div>
                    <div className={"search-stock"}>{stock.name}</div>
                </div>
                <div>
<div>
                    <div className={"search-stock"}>{stock.symbol}</div>

                    <button className={"addPage-button select-button"} type={"submit"}>select</button>
</div>
                </div>
            </form>
        </div>
    )
}