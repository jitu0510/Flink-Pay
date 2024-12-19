import Axios from "axios";

const uri = process.env.REACT_APP_APP_URL;

export const transferAmount = (object) => {
    
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.post(`${uri}/amount-transfer/${username}`, object);
}