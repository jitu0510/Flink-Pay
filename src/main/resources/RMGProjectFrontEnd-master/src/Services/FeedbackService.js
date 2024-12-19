import Axios from "axios";


export const addFeedback = (feedbackData) => {
    return Axios.post('http://49.249.29.5:8091/feedback', feedbackData);
}