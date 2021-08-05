import React from 'react';
import axios from 'axios';
import Auth from '../services/security/auth';
import { Redirect } from 'react-router-dom';

const LOGIN_URL = 'http://localhost:8080/login';

class Login  extends React.Component {
    
    state = {
        username: "",
        password: "",
        redirect: false,
    }

    constructor(props) {
        super(props);
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value,
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        const {username, password} = this.state;
        axios.post(LOGIN_URL, {username,password})
        .then(res => {console.log(res);  
            Auth.logIn(res.headers.authorization);
            this.setState({redirect: true});
            this.props.getUsername(username); 
            this.props.refreshAsLoggedIn();})
        .catch(err => console.log(err));
        
    }

    isValid() {
        return this.state.username != '' && this.state.password != '';
    }

    render() {
        const {redirect} = this.state;
        if (redirect) {
            return <Redirect to="/home"/>
        }
        return (
            <div className="create-user-page">
            <h1>please log in</h1>
            <form onSubmit={this.handleSubmit} className="custom-form">
            <table>
            <tbody>
            <tr>
                <td><label htmlFor="username">Username</label></td>
                <td><input id="username" name="username" type="text" value={this.state.username} onChange={this.handleInputChange}/></td>
            </tr>
            <tr>
                <td><label htmlFor="password">Password</label></td>
                <td><input id="password" name="password" type="password" value={this.state.password} onChange={this.handleInputChange}/></td>
            </tr>
            </tbody>
            </table>
            <input type="submit" value="submit" disabled={!this.isValid()}/>
            </form>
            </div>
        )
    }
}
 
export default Login;