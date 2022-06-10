import {Link} from "react-router-dom";
import "../styles/NavBar.css";
import logo from "../images/logo2.png";
import {BsFillPlusCircleFill} from "react-icons/bs";

export default function NavBar() {
    return (
        <div className={"navbar"}>
            <div>
                <div className={"logo-container"}>
                    <Link to={"/"}><img className={"logo"} src={logo} alt="Logo"/></Link>
                    <div className={"logo-text"}>Portfolio-Tracker</div>
                </div>
                <div className={"addStock-container"}>
                    <Link to={"/addStock"}><BsFillPlusCircleFill viewBox={"0 0 22 22"} className={"addStock1"}/></Link>
                </div>
            </div>
        </div>
    )
}
