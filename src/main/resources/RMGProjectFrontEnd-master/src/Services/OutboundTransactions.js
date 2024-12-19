import Axios from "axios";

const uri = process.env.REACT_APP_APP_URL;

export const getAllOutbounds = async (activePage) => {
    const username = localStorage.getItem("usernameDecrypt");
    try {
        // Get the total number of projects
        const numberOfOutboundResponse = await OutboundCount();
        const numberOfOutbounds = numberOfOutboundResponse.data;

        // Use the total number of projects as the size parameter
        const response = await Axios.get(`${uri}/amount-transfer/all/${username}?page=${activePage}&size=${numberOfOutbounds}`);
       // const response = await Axios.get(`${uri}/amount-transfer/all-outbounds/${username}`);

        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching Transactions:', error);
        throw error;
    }
};
export const OutboundCount = () => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/amount-transfer/count/${username}`);
}

export const getPaginatedOutbounds = (activePage) => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/amount-transfer/all/${username}?page=${activePage}&size=10`);
}
export const downloadSwiftFile = (referenceNumber) => {
    const username = localStorage.getItem("usernameDecrypt");
    return Axios.get(`${uri}/amount-transfer/download/${username}?referenceNumber=${referenceNumber}`, {
      headers: {
        Accept: 'application/octet-stream',
      },
      responseType: 'blob', // Specify response type as blob for binary data
    });
  };

// await fetch(
//     `http://localhost:8094/amount-transfer/download?referenceNumber=${referenceNumber}`, // Replace with your API endpoint
//     {
//       method: 'GET',
    //   headers: {
    //     Accept: 'application/octet-stream',
    //   }
//     }
//   );