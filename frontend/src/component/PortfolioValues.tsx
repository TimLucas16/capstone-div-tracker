import {Portfolio} from "../model/Portfolio";

type PortfolioProps = {
    pfValues : Portfolio
}

export default function PortfolioValues({pfValues} : PortfolioProps) {

    return (
        <div className={"pfValues"}>
            <div>{(pfValues.pfValue / 100).toFixed(2)} $</div>
            <div>{(pfValues.pfTotalReturnAbsolute / 100).toFixed(2)} $</div>
            <div>{(pfValues.pfTotalReturnPercent).toFixed(2)} %</div>
        </div>
    )
}