import {Doughnut} from "react-chartjs-2";
import "./DonutChart.css";

type DonutProps = {
    values: number[]
    names: string[]
}

export default function DonutChart({values, names}: DonutProps) {

    const data = {
        datasets: [
            {
                data: values,
                backgroundColor: [
                    '#707B52',
                    '#BDC4A7',
                    '#8E6E53',
                    '#615A5B',
                    '#92B4A7',
                    '#93B5C6',
                    '#518AA7',
                    '#B56B45',
                ],
            }
        ],
    }

    const options = {
        cutout:"70%",

    }

    return (
        <div id={"donutchart"}>
            <Doughnut data={data} options={options}/>
        </div>
    )
}