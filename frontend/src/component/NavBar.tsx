import {Link} from "react-router-dom";

export default function NavBar() {
    return (
        <div>
            <h1>Div-Tracker</h1>
            <Link to={"/addStock"}>add Stock</Link>
        </div>
    )
}