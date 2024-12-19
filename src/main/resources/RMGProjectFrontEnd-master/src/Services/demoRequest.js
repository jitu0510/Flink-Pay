import Axios from "axios";


export const requestDemo = (demoRequestData) => {
    return Axios.post('http://49.249.29.5:8091/demorequest', demoRequestData);
}