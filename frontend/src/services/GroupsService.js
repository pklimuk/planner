import axios from 'axios';
import headers from './security/headers';

const GROUPS_REST_API_URL = 'http://localhost:8080/api/user/groups';

class GroupsService {

    getGroups(){
        const config = {
            headers: headers(),
        }
        return axios.get(GROUPS_REST_API_URL, config);
    }


    getEventsDeadlines(group) {
        const groupName = {
            group_name: group.group_name,
        };
        const config = {
            headers: headers(),
        }
        console.log(config);
        console.log(groupName);
        return axios.post(GROUPS_REST_API_URL+'/deadlines_and_events', groupName, config);
    }

    editGroup = async(group, newData) => {
        const config = {
            headers: headers(),
        }
        const editedGroup = {
            group_name: group.group_name,
            new_group_name: newData.group_name,
            new_group_description: newData.group_description
        }
        let res = await axios.put(GROUPS_REST_API_URL, editedGroup, config)
    }

    postGroup(group) {
        const config = {
            headers: headers(),
        }
        return axios.post(GROUPS_REST_API_URL, group, config);     
    }

    deleteGroup(group) {
        const group_name = group.group_name;
        const config = {
            headers: headers(),
            data: {
                group_name: group_name,
            }
        }
        console.log(config);
        return axios.delete(GROUPS_REST_API_URL, config);
    }

}

export default new GroupsService;