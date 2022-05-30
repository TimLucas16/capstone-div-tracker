import * as React from 'react';
import StockCard from "../component/StockCard";
import {Stock} from "../model/Stock";
import {Portfolio} from "../model/Portfolio";
import "../styles/StartPage.css";
import {useEffect, useState} from "react";
import StockCardLegend from "../component/StockCardLegend";
import OverviewCard from "../component/OverviewCard";


export type StartPageProps = {
    stocks: Stock[]
    pfValues: Portfolio
}

export default function StartPage({stocks, pfValues}: StartPageProps) {
    const [values, setValues] = useState<number[]>([])
    const [names, setNames] = useState<string[]>([])

    useEffect(() => {
        setValues([...stocks.map(value => value.value)])
        setNames([...stocks.map(names => names.companyName)])
    }, [stocks])

    return (
        <div className={"startpage-container"}>
            <OverviewCard values={values}
                          names={names}
                          pfValues={pfValues} />
            <StockCardLegend/>
            <div> {stocks.map(stock => <StockCard key={stock.symbol} stock={stock} pfValue={pfValues.pfValue}/>)} </div>
        </div>
    )
}