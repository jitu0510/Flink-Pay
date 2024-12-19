import Axios from "axios";
import {getToken} from  "./AuthenticationService";




export const getNumberOfEmployees = () => {
    return Axios.get('http://49.249.29.5/:8091/count-employees');
};
export const getAllEmployes = (activePage) =>{
    return Axios.get(`http://49.249.29.5:8091/employees?page=${activePage}&size=10`,{ headers: { Authorization: 'Bearer '+getToken() } });
}
// export const getEmployees = (activePage) => {
//     return Axios.get(`http://49.249.29.5:8091/all-employees?page=${activePage}&size=${getNumberOfEmployees}`,{ headers: { Authorization: 'Bearer '+getToken() } });
// }
export const getEmployees = async (activePage) => {
    try {
        // Get the total number of employees
        const numberOfEmployeesResponse = await getNumberOfEmployees();
        const numberOfEmployees = numberOfEmployeesResponse.data;

        // Use the total number of employees as the size parameter
        const response = await Axios.get(`http://49.249.29.5:8091/all-employees?page=${activePage}&size=${numberOfEmployees}`, {
            headers: { Authorization: 'Bearer ' + getToken() }
        });

        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching employees:', error);
        throw error;
    }
};



// export const addUser = (user) => {
//     return Axios.post('http://49.249.29.5:8091/signup', user,{ headers: { Authorization: 'Bearer '+getToken() } });
// }
export const addUser = (user) => {
    return Axios.post('http://49.249.29.5:8091/signup', user);
}

export const checkUserIsPresent = (username) => {
    return Axios.get(`http://49.249.29.5:8091/signup/${username}`,{ headers: { Authorization: 'Bearer '+getToken() } });
}

export const addEmployeeapi = (employee) => {
    return Axios.post('http://49.249.29.5:8091/employees', employee,{ headers: { Authorization: 'Bearer '+getToken() } });
}

export const getEmpDetails = (userName)=>{
    console.log(userName)
    return Axios.get(`http://localhost:8094/employee/${userName}`,{ headers: { Authorization: 'Bearer '+getToken() } });
}

export const deleteEmployee = (employeeId)=>{
    console.log(employeeId+" is rquested to be deleted")
    return Axios.delete(`http://49.249.29.5:8091/employee/${employeeId}`,{ headers: { Authorization: 'Bearer '+getToken() } });
}

export const updateEmployee=(employeeId,updatedBody)=>{
    console.log(updatedBody);
    console.log(employeeId);
    return Axios.put(`http://49.249.29.5:8091/employee/${employeeId}`,updatedBody,{ headers: { Authorization: 'Bearer '+getToken() } });
}
export const getEmployeesExperienceData = () => {
    return Axios.get('http://49.249.29.5:8091/employee/getExperience',{ headers: { Authorization: 'Bearer '+getToken() } });
}

