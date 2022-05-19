import React from 'react';
import './App.css';
import {Route, Routes} from 'react-router-dom';
import StartPage from "./pages/StartPage";
import NewStock from "./component/NewStock";
import useStocks from "./hooks/useStocks";

export default function App() {

    const {addStock} = useStocks()

    return (
        <div className="App">
            <Routes>
                <Route path="/" element={<StartPage />}/>
                <Route path="/addStock" element={<NewStock addStock={addStock}/>}/>
            </Routes>
        </div>
    );
}