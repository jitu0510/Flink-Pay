import Axios from "axios";
import {getToken} from  "./AuthenticationService";


const uri = process.env.REACT_APP_APP_URL;


export const addProject = (project) => {
    return Axios.post(`${uri}/addProject`, project);
}

export const getProjects = () => {
    return Axios.get(`${uri}/projects`);
}
export const getPaginatedProjects = (activePage) => {
    return Axios.get(`http://49.249.29.5:8091/projects1?page=${activePage}&size=10`);
}
export const getAllProjects = async (activePage) => {
    try {
        // Get the total number of projects
        const numberOfProjectsResponse = await projectCount();
        const numberOfProjects = numberOfProjectsResponse.data;

        // Use the total number of projects as the size parameter
        const response = await Axios.get(`http://49.249.29.5:8091/projects1?page=${activePage}&size=${numberOfProjects}`);
        return response;
    } catch (error) {
        // Handle errors appropriately
        console.error('Error fetching projects:', error);
        throw error;
    }
};

export const projectCount = () => {
    return Axios.get(`http://49.249.29.5:8091/count-projects`);
}
export const deleteProject=(projectId)=>{
    return Axios.delete(`http://49.249.29.5:8091/projects/${projectId}`);
}

export const updateProject=(projectId,updateBody)=>{
    return Axios.put(`http://49.249.29.5:8091/projects/${projectId}`,updateBody);
}

export const loadProjectsNameAndId = () => {
    return Axios.get('http://49.249.29.5:8091/projects');
}

export const exportProject = () => {
   return Axios.get('http://49.249.29.5:8091/export');
}

export const getEmployeesExperienceData = () => {
    return Axios.get('http://49.249.29.5:8091/employee/getExperience');
}