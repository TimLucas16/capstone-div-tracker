import {Portfolio} from "../model/Portfolio";

type PortfolioProps = {
    pfValues: Portfolio
}

export default function PortfolioValues({pfValues}: PortfolioProps) {

    return (
        <div className={"pfValues-container"}>
            <div>
                <div className={"pfTitel"}>Portfolio Value</div>
                <div className={"pfValues"}>
                    {(pfValues.pfValue).toFixed(2).replace(".", ",")} $</div>
            </div>
            <div>
                <div className={"pfTitel"}>Total Return</div>

                {pfValues.pfTotalReturnAbsolute < 0
                    ? <div>
                        <div className={"pfValues negative"}>
                            {(pfValues.pfTotalReturnAbsolute).toFixed(2).replace(".", ",")} $</div>
                        <div className={"pfValues negative"}>
                            {(pfValues.pfTotalReturnPercent).toFixed(2).replace(".", ",")} %</div>
                    </div>

                    : <div>
                        <div className={"pfValues"}>
                            {(pfValues.pfTotalReturnAbsolute).toFixed(2).replace(".", ",")} $</div>
                        <div className={"pfValues"}>
                            {(pfValues.pfTotalReturnPercent).toFixed(2).replace(".", ",")} %</div>
                    </div>
                }

            </div>
        </div>
    )
}