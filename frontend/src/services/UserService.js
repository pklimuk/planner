import axios from 'axios';
import headers from './security/headers';

const USERS_POST_REST_API_URL = 'http://localhost:8080/api/registration';
const USERS_GET_REST_API_URL = 'http://localhost:8080/api/user/userprofile';
const USER_REST_API_URL = 'http://localhost:8080/api/user';


class UserService {

    userInfo = {
    };

    getInfo(info){
        Object.assign(this.userInfo, info);
        delete this.userInfo.redirect;
    }

    postUser = async() => {
        let res = await axios.post(USERS_POST_REST_API_URL, this.userInfo);
        console.log(res);
    }

    deleteUser() {
        const config = {
            headers: headers(),
        }
        return axios.delete(USER_REST_API_URL, config);
    }

    getProfile() {
        const config = {
            headers: headers(),
        }
        return axios.get(USERS_GET_REST_API_URL, config);
    }

    changeProfile(newProfile) {
        const config = {
            headers: headers(),
        }
        return axios.put(USERS_GET_REST_API_URL, newProfile, config);
    }

    changeProfileImage(data) {
        const config = {
            headers: headers(),
        }
        config.headers['Content-Type'] = 'multipart/form-data';
        console.log(config);
        console.log(data);
        return axios.put(USERS_GET_REST_API_URL + '/update_image', data, config);
    }

}

export default new UserService();