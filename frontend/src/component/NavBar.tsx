

import {Link} from "react-router-dom";
import "../styles/NavBar.css";

export default function NavBar() {
    return (
        <div className={"navbar"}>
            <h1>Div-Tracker</h1>
            <Link to={"/addStock"}>add Stock</Link>
        </div>
    )
}