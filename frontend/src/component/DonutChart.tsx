import {Doughnut} from "react-chartjs-2";
import "../styles/DonutChart.css";
import {Chart as ChartJS, ArcElement, Tooltip, Legend, Chart} from 'chart.js';

ChartJS.register(ArcElement, Tooltip, Legend);

type DonutProps = {
    values: number[]
    names: string[]
}

export default function DonutChart({values, names}: DonutProps) {

    const hoverLabel = {
        id: 'hoverLabel',
        afterDraw(chart: Chart) {
            const {ctx, chartArea: {width, height}} = chart;
            ctx.save();

            ctx.font = "bolder 50px Arial";
            ctx.fillStyle = "blue";
            ctx.textAlign = "center";
            ctx.fillText("Test", width / 2, height / 2)
        }
    };

    const data = {
        datasets: [
            {
                data: values,
                labels: names,
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
                hoverBackgroundColor: [
                    "#FF6283",
                ],
            }
        ],
        title: {
            display: true,
            text: "Donut Titel"
        },
    }

    const options = {
        cutout: "70%",
    }

    return (
        <Doughnut data={data} options={options}/>
    )
}