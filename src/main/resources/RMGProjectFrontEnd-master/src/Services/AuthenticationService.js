import Axios from "axios";

var CryptoJS = require("crypto-js");

export const login = ({ username, password }) => {
    let basicAuthHeader = 'Basic ' + window.btoa(username + ":" + password)
    const baseUrl = process.env.REACT_APP_APP_URL;
   // console.log(basicAuthHeader); http://49.249.29.5/
    //Axios.get('http://49.249.29.5/login', { headers: { Authorization: basicAuthHeader } }).then(console.log("Logged In")).catch(console.log("Error"));
    const apiUrl = `${baseUrl}/login`;
    return Axios.get(apiUrl, { headers: { Authorization: basicAuthHeader } });
}

export const registerSuccessfulLogin = (username, role,jwt) => {
    var usernameCrypt = CryptoJS.AES.encrypt(JSON.stringify(username), 'secret').toString();
    var roleCrypt = CryptoJS.AES.encrypt(JSON.stringify(role), 'secret').toString();
    var jwtLocal=jwt
    localStorage.setItem("username", usernameCrypt)
    localStorage.setItem("role", roleCrypt)
    localStorage.setItem("roleDecrypt",role)
    localStorage.setItem("usernameDecrypt",username)
    localStorage.setItem("jwt",jwtLocal)
    localStorage.setItem("loginTime",new Date().toLocaleString())
}

export const isUserLoggedIn = () => {
    let user = localStorage.getItem('username')
    if (user === null) return false
    return true
}

export const getUserName = () =>{

    let user = localStorage.getItem('username')
    if (user === null) return false
    var bytes  = CryptoJS.AES.decrypt(user, 'secret');
    var originalText = bytes.toString(CryptoJS.enc.Utf8);
    return originalText;

}

export const getToken = () =>{
    let jwt = localStorage.getItem('jwt')
    if (jwt === null) return false
    return jwt;
}