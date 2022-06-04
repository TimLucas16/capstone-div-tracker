import {Link} from "react-router-dom";
import "../styles/NavBar.css";
import logo from "../images/logo.png";
import { BsFillPlusCircleFill } from "react-icons/bs";

export default function NavBar() {
    return (
        <div className={"navbar"}>
            <Link to={"/"}><img className={"logo"} src={logo} alt="Logo"/></Link>
            <Link to={"/addStock"}><BsFillPlusCircleFill className={"addStock"}/></Link>
        </div>
    )
}