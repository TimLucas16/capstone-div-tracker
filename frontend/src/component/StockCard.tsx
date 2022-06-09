import {Stock} from "../model/Stock";
import "../styles/StockCard.css";
import {useCallback, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {FcComboChart} from "react-icons/fc";

type StockProp = {
    stock: Stock
    pfValue: number
}

export default function StockCard({stock, pfValue}: StockProp) {
    const negativeClassName = stock.totalReturn < 0 && " negative"
    const navigate = useNavigate()

    const [allocation, setAllocation] = useState<number>(0)

    const allocationChanged = useCallback(() =>
        setAllocation((stock.value / pfValue) * 100), [stock.value, pfValue]);

    useEffect(() => {
        allocationChanged()
    }, [allocationChanged])

    return (
        <div className={"card-container"}>
            <div className={"card-details"}>
                <a className={"anker"} href={stock.website}>
                    <img className={"startpage-logo"} src={stock.image} alt={stock.companyName}/>
                </a>
                <div className={"name"}> {stock.companyName} </div>
                <div className={"isin"}> {stock.isin}</div>
                <div className={"button-editPage"}>
                    <button onClick={() => navigate(`/updateStock/${stock.id}`)}><FcComboChart/></button>
                </div>
                <div className={"price"}> {(stock.price).toFixed(2).replace(".", ",")} $</div>
                <div className={"value"}> {(stock.value).toFixed(2).replace(".", ",")} $</div>

                <div className={"total-return-container"}>
                    <div className={"total-return" + negativeClassName}> {(stock.totalReturn).toFixed(2).replace(".", ",")} $</div>
                </div>

                <div className={"allocation"}> {allocation.toFixed(2).replace(".", ",")} %</div>
                <div className={"shares"}> {stock.shares} x</div>
            </div>

        </div>
    )
}