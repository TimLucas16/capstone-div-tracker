import {StockDto} from "../model/StockDto";
import {useParams} from "react-router-dom";
import {FormEvent, useEffect, useState} from "react";
import {Stock} from "../model/Stock";
import "../styles/UpdateStockPage.css";

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

    const submit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault()
        if (amount === 0 || costPrice === 0) {
            alert("Bitte bei Shares oder CostPrice eine Zahl größer 0 eingeben!")
        }
        const stockChanges: StockDto = {
            symbol: stock.symbol,
            shares: amount,
            costPrice: costPrice * 100
        }
        updateStock(stockChanges)
    }


    return (
        <div>

            <div className={"stock-details-container"}>
                <div>
                    <a href={stock.website}>
                        <img className={"editPage-logo"} src={stock.image} alt={stock.companyName}/>
                    </a>
                </div>
                <div>
                    <div>{stock.companyName}</div>
                    <div>
                        <div className={"editPage-shares"}>{stock.shares} shares</div>
                        <button onClick={() => setAmount(stock.shares)}
                                className={"addPage-button editPage-button"}>select all
                        </button>
                    </div>
                </div>
            </div>


            <form className={"update-form"} onSubmit={submit}>

                <div>
                    <input id={"buy"} type="radio" name={"action"}/>
                    <label className={"edit-label"} htmlFor={"buy"}>buy</label>

                    <input id={"sell"} type="radio" name={"action"}/>
                    <label className={"edit-label"} htmlFor={"sell"}>sell</label>
                </div>

                <input type="hidden" value={stock.symbol} disabled/>

                <input type="number" placeholder={"amount"} value={amount}
                       onChange={event => setAmount(Number(event.target.value))}/>

                <input type="number" placeholder={"costPrice"}
                       onChange={event => setCostPrice(Number(event.target.value))}/>
                <button type={"submit"} className={"addPage-button editPage-button"}>submit</button>
            </form>


        </div>
    )
}