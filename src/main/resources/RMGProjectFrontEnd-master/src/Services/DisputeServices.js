import Axios from "axios";

const uri = process.env.REACT_APP_APP_URL;

export const raiseDispute = (referenceNumber,description) => {
    
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.post(`${uri}/dispute/raise/${username}?description=${description}&referenceNumber=${referenceNumber}`);
}

export const getAllDisputes = async (activePage) => {
    const username = localStorage.getItem("usernameDecrypt");
    try {
        // Get the total number of projects
        const numberOfDisputesResponse = await disputeCount();
        const numberOfDisputes = numberOfDisputesResponse.data;

        // Use the total number of projects as the size parameter
        const response = await Axios.get(`${uri}/dispute/all/${username}?page=${activePage}&size=${numberOfDisputes}`);
       // const response = await Axios.get(`${uri}/amount-transfer/all-outbounds/${username}`);

        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching Transactions:', error);
        throw error;
    }
};
export const disputeCount = () => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/dispute/count/${username}`);
}

export const getPaginatedDisputes = (activePage) => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/dispute/all/${username}?page=${activePage}&size=10`);
}