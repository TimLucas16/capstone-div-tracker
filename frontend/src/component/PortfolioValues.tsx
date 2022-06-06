import {Portfolio} from "../model/Portfolio";

type PortfolioProps = {
    pfValues: Portfolio
}

export default function PortfolioValues({pfValues}: PortfolioProps) {

    return (
        <div className={"pfValues-container"}>
            <div>
                <div className={"pfTitel"}>Portfolio Value</div>
                <div className={"pfValues"}>{(pfValues.pfValue / 100).toFixed(2)} $</div>
            </div>
            <div>
                <div className={"pfTitel"}>Total Return</div>
                <div className={"pfValues"}>{(pfValues.pfTotalReturnAbsolute / 100).toFixed(2)} $</div>
                <div className={"pfValues"}>{(pfValues.pfTotalReturnPercent).toFixed(2)} %</div>
            </div>
        </div>
    )
}