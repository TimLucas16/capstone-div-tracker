import React from 'react';
import './App.css';
import {Route, Routes} from 'react-router-dom';
import StartPage from "./pages/StartPage";
import NewStockPage from "./pages/NewStockPage";
import useStocks from "./hooks/useStocks";
import NavBar from "./component/NavBar";
import UpdateStockPage from './pages/UpdateStockPage';

export default function App() {

    const {stocks, addStock, pfValues, updateStock, stock, getStockById, searchForStock, stockList, selectedStock, selectStock} = useStocks()

    return (
        <div className="App">
            <NavBar/>
            <Routes>

                <Route path="/"
                       element={<StartPage
                           stocks={stocks}
                           pfValues={pfValues}/>}/>

                <Route path="/addStock"
                       element={<NewStockPage
                           addStock={addStock}
                           searchForStock={searchForStock}
                           stockList={stockList}
                           selectedStock={selectedStock}
                           selectStock={selectStock}/>}/>

                <Route path="/updateStock/:id"
                       element={<UpdateStockPage
                           updateStock={updateStock}
                           getStockById={getStockById}
                           stock={stock}/>}/>

            </Routes>
        </div>
    );
}