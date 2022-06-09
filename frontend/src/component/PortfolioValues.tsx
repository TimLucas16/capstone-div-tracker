import {Portfolio} from "../model/Portfolio";

type PortfolioProps = {
    pfValues: Portfolio
}

export default function PortfolioValues({pfValues}: PortfolioProps) {

    const negativeClassName = (pfValues.pfTotalReturnAbsolute < 0) && " negative";

    return (
        <div className={"pfValues-container"}>
            <div>
                <div className={"pfTitel"}>Portfolio Value</div>
                <div className={"pfValues"}>
                    {(pfValues.pfValue).toFixed(2).replace(".", ",")} $
                </div>
            </div>
            <div>
                <div className={"pfTitel"}>Total Return</div>

                <div className={"pfValues" + negativeClassName}>
                    {(pfValues.pfTotalReturnAbsolute / 100).toFixed(2).replace(".", ",")} $
                </div>
                <div className={"pfValues" + negativeClassName}>
                    {(pfValues.pfTotalReturnPercent).toFixed(2).replace(".", ",")} %
                </div>

            </div>
        </div>
    )
}
