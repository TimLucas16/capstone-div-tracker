
export default function StockCardLegend() {
    return (
        <div className={"card-container"}>
            <div className={"card-legend"}>
                <div className={"price"}> Price</div>
                <div className={"value"}> Value</div>
                <div className={"total-return-Percent-container"}>%</div>
                <div className={"total-return-container"}> Total Return</div>
                <div className={"allocation"}> Allocation</div>
                <div className={"shares"}> Shares</div>
            </div>
        </div>
    )
}