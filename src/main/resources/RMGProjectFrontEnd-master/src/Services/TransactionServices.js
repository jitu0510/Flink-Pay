import Axios from "axios";

const uri = process.env.REACT_APP_APP_URL;

export const getAllTransactions = async (activePage) => {
    try {
        // Get the total number of projects
        const numberOfProjectsResponse = await TransactionCount();
        const numberOfProjects = numberOfProjectsResponse.data;

        // Use the total number of projects as the size parameter
        const response = await Axios.get(`${uri}/all-transactions?page=${activePage}&size=${numberOfProjects}`);

        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching Transactions:', error);
        throw error;
    }
};
export const TransactionCount = () => {
    return Axios.get(`${uri}/count-transactions`);
}

export const getPaginatedTransactions = (activePage) => {
    return Axios.get(`${uri}/all-transactions?page=${activePage}&size=10`);
}