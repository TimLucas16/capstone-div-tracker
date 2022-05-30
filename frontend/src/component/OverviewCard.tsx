
import * as React from 'react';
import DonutChart from "./DonutChart";
import PortfolioValues from "./PortfolioValues";
import {Portfolio} from "../model/Portfolio";
import "../styles/OverviewCard.css";

type OverviewProps = {
    values: number[]
    names: string[]
    pfValues : Portfolio
}

export default function OverviewCard({values, names, pfValues} : OverviewProps) {

    return (
        <div className={"chartValueContainer"}>
            <div>
                < DonutChart
                    values={values}
                    names={names} />
                < PortfolioValues pfValues={pfValues} />
            </div>
        </div>
    )
}