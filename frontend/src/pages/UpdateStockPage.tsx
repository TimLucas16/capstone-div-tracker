import {StockDto} from "../model/StockDto";
import {useParams} from "react-router-dom";
import {FormEvent, useEffect, useState} from "react";
import {Stock} from "../model/Stock";

type UpdateProps = {
    updateStock: (updateStock: StockDto) => void
    getStockById: (id: string) => void
    stock: Stock
}

export default function UpdateStockPage({updateStock, getStockById, stock}: UpdateProps) {
    const {id} = useParams()

    const [amount, setAmount] = useState<number>(0)
    const [costPrice, setCostPrice] = useState<number>(0)

    useEffect(() => {
        if (id) {
            getStockById(id)
        }// eslint-disable-next-line
    }, [])

    const submit = (event : FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if(amount === 0 || costPrice === 0) {
            alert("Bitte bei Shares oder CostPrice eine Zahl größer 0 eingeben!")
        }
        const stockChanges : StockDto = {
            symbol : stock.symbol,
            shares : amount,
            costPrice : costPrice*100
        }
        updateStock(stockChanges)
    }

    return (
        <div>
            <a className={"anker"} href={stock.website}>
                <img className={"logo"} src={stock.image} alt={stock.companyName}/>
            </a>
            <div>{stock.companyName}</div>
            <div>{stock.shares} shares</div>

            <form onSubmit={submit}>
                <input type="hidden" value={stock.symbol} disabled/>

                <input type="number" id="shares" placeholder={`${stock.shares}`}
                       onChange={event => setAmount(Number(event.target.value))}/>

                <input type="number" placeholder={"costPrice"}
                       onChange={event => setCostPrice(Number(event.target.value))}/>
                <button type={"submit"}>submit</button>
            </form>
        </div>
    )
}