import Axios from "axios";
import {getToken} from  "./AuthenticationService";
const uri = process.env.REACT_APP_APP_URL;

export const getAllInbounds = async (activePage) => {
    try {
        // Get the total number of projects
        const numberOfInboundResponse = await InboundCount();
        const numberOfInbounds = numberOfInboundResponse.data;
        const username = localStorage.getItem("usernameDecrypt");
        // Use the total number of projects as the size parameter
        const response = await Axios.get(`${uri}/inbound-transaction/all/${username}?page=${activePage}&size=${numberOfInbounds}`);
        
        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching Transactions:', error);
        throw error;
    }
};
export const InboundCount = () => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/inbound-transaction/count/${username}`);
}

export const getPaginatedInbounds = (activePage) => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/inbound-transaction/all/${username}?page=${activePage}&size=10`);
}