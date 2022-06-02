import {FormEvent, useState} from "react";
import {StockDto} from "../model/StockDto";

type NewStockProps = {
    addStock : (newStock : StockDto) => void
}

export default function NewStockPage({addStock} : NewStockProps) {

    const [symbol, setSymbol] = useState<string>("")
    const [amount, setAmount] = useState<number>(0)
    const [costPrice, setCostPrice] = useState<number>(0)

    const submit = (event : FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if(!symbol) {
            alert("Bitte bei Name oder Symbol etwas eingeben!")
        }
        if(amount <= 0 || costPrice <= 0) {
            alert("Bitte bei Amount oder Course eine Zahl größer 0 eingeben!")
        }
        const newStock : StockDto = {
            symbol : symbol,
            shares : amount,
            costPrice : costPrice*100
        }
        addStock(newStock)
    }

    return (
        <form onSubmit={submit}>
            <input type="text" placeholder={"symbol"} value={symbol} onChange={event => setSymbol(event.target.value)}/>
            <input type="number" placeholder={"amount"} value={amount} onChange={event => setAmount(Number(event.target.value))}/>
            <input type="number" placeholder={"costPrice"} value={costPrice} onChange={event => setCostPrice(Number(event.target.value))}/>
            <button type={"submit"}>submit</button>
        </form>
    )
}