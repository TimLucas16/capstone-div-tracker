import {Pie} from "react-chartjs-2";
import "../styles/OverviewCard.css";
import {Chart as ChartJS, ArcElement, Tooltip, Legend} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

type DonutProps = {
    values: number[]
    names: string[]
}

export default function DonutChart({values}: DonutProps) {

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

    return (
        <div id={"donutchart"}>
            <Pie data={data}/>
        </div>
    )
}
