import axios from 'axios';
import headers from './security/headers';

const EVENTS_REST_API_URL = 'http://localhost:8080/api/user/events';

class EventsService {

    getEvents(){
        const config = {
            headers: headers(),
        }
        return axios.get(EVENTS_REST_API_URL, config);
    }

    postEvent = async(event) => {
        const config = {
            headers: headers(),
        }
        let res = await axios.post(EVENTS_REST_API_URL, event, config);
        console.log(res);
    }

    deleteEvent(event) {
        const eventToDelete = event;
        const config = {
            headers: headers(),
        }
        config.data = eventToDelete;
        return axios.delete(EVENTS_REST_API_URL, config);
    }

    editEvent(event, newData) {
        const config = {
            headers: headers(),
        }
        if (newData.groups.length == 0) {
            newData.groups.push("CLEAR ALL");
        }
        const editedEvent = {
            title: event.title,
            start: event.start,
            end: event.end,
            description: event.description,
            new_title: newData.title,
            new_start: newData.start,
            new_end: newData.end,
            new_description: newData.description,
            new_groups: newData.groups,
        }
        return axios.put(EVENTS_REST_API_URL, editedEvent, config);
    }

}

export default new EventsService;