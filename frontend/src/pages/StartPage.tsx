import StockCard from "../component/StockCard";
import {Stock} from "../model/Stock";
import {Portfolio} from "../model/Portfolio";
import DonutChart from "../component/DonutChart";
import "./StartPage.css";
import {useEffect, useState} from "react";
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';
import StockCardLegend from "../component/StockCardLegend";
import PortfolioValues from "../component/PortfolioValues";

ChartJS.register(ArcElement, Tooltip, Legend);

export type StartPageProps = {
    stocks: Stock[]
    pfValues: Portfolio
}

export default function StartPage({stocks, pfValues}: StartPageProps) {
    const [values, setValues] = useState<number[]>([])
    const [names, setNames] = useState<string[]>([])

    useEffect(() => {
        getValues()
        getNames()
    }, [stocks])

    const getValues = () => {
        setValues([...stocks.map(value => value.value)])
    }

    const getNames = () => {
        setNames([...stocks.map(names => names.companyName)])
    }

    return (
        <div className={"startpage-container"}>
            <div className={"chartValueContainer"}>
                <div className={"nochnscheisscontainer"}>
                    <DonutChart values={values} names={names}/>
                    <PortfolioValues pfValues={pfValues}/>
                </div>
            </div>
            <StockCardLegend/>
            <div> {stocks.map(stock => <StockCard key={stock.symbol} stock={stock}/>)} </div>
        </div>
    )
}