import axios from 'axios';
import headers from './security/headers';

const DEADLINES_REST_API_URL = 'http://localhost:8080/api/user/deadlines';

class DeadlinesService {

    getDeadlines(){
        const config = {
            headers: headers(),
        }
        return axios.get(DEADLINES_REST_API_URL, config);
    }

    postDeadline = async(deadline) => {
        const config = {
            headers: headers(),
        }
        let res = await axios.post(DEADLINES_REST_API_URL, deadline, config);
        console.log(res);
    }

    deleteDeadline(deadline) {
        const deadlineToDelete = deadline;
        const config = {
            headers: headers(),
        }
        config.data = deadlineToDelete;
        return axios.delete(DEADLINES_REST_API_URL, config);
    }

    editDeadline(deadline, newData) {
        const config = {
            headers: headers(),
        }
        if (newData.groups.length == 0) {
            newData.groups.push("CLEAR ALL");
        }
        const editedDeadline = {
            title: deadline.title,
            deadline_time: deadline.deadline_time,
            description: deadline.description,
            new_title: newData.title,
            new_time: newData.deadline_time,
            new_description: newData.description,
            new_groups: newData.groups,
        }
        console.log(editedDeadline);
        return axios.put(DEADLINES_REST_API_URL, editedDeadline, config);
    }

}

export default new DeadlinesService;