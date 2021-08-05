import {checkResponseStatus} from './../handlers/responseHandlers';
import headers from './../security/headers';
import 'whatwg-fetch';
import qs from 'qs';

const SERVER_URL = "http://localhost:8080";


export default {
  logIn(auth) { 
    localStorage.setItem('auth', auth);
  },

  logOut() { 
    localStorage.removeItem('auth');
  },

  loggedIn() {  
    return localStorage.getItem('auth');
  }
};