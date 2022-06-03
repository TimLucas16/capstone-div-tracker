import axios from "axios";

const apikey = "9eafa28db1b3dd701c8d112b5bbe75d9"

export const stockSearch = (company : string) => {
    return axios.get("https://financialmodelingprep.com/api/v3/search?query=" + company + "&limit=10&exchange=NASDAQ&apikey=" + apikey)
        .then(response => response.data)
}