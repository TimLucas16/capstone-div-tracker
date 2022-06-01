import {StockDto} from "../model/StockDto";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
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
        }
    }, [])


    return (
        <div>
            <a className={"anker"} href={stock.website} target="_blank">
                <img className={"logo"} src={stock.image} alt={stock.companyName}/>
            </a>
            <div>{stock.companyName}</div>
            <div>{stock.shares} shares</div>






            <form>
                <input type="hidden" value={stock.symbol} disabled/>


                <input type="number" id="shares" placeholder={`${stock.shares}`} value={amount}
                       onChange={event => setAmount(Number(event.target.value))}/>

                <input type="number" placeholder={"costPrice"} value={costPrice}
                       onChange={event => setCostPrice(Number(event.target.value))}/>
                <button type={"submit"}>submit</button>
            </form>
        </div>
    )
}