import React from 'react';
import './App.css';
import {Route, Routes} from 'react-router-dom';
import StartPage from "./pages/StartPage";
import NewStock from "./component/NewStock";
import useStocks from "./hooks/useStocks";
import NavBar from "./component/NavBar";

export default function App() {

    const {stocks, addStock, pfValues} = useStocks()

    return (
        <div className="App">
            <NavBar />
            <Routes>
                <Route path="/"
                       element={<StartPage
                           stocks={stocks}
                           pfValues={pfValues}/>}/>
                <Route path="/addStock"
                       element={<NewStock
                           addStock={addStock}/>}/>
            </Routes>
        </div>
    );
}