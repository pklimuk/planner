import React from 'react';
import UserService from '../services/UserService';


class Users extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            users:[]
        }
    }

    componentDidMount(){
        UserService.getUsers().then((response) => {
            this.setState({ users: response.data})
        })
    }

    render (){
        return (
            <div className="userslist-page">
                <h1>users list</h1>
                <table className="userslist">
                    <thead>
                        <tr>
                            <td> User First Name</td>
                            <td> User Last Name</td>
                            <td> User Email</td>
                            <td> User's Date of Birth</td>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.state.users.map(
                                user =>
                                <tr>
                                    <td> {user.firstName}</td>
                                    <td> {user.lastName}</td>
                                    <td> {user.email}</td>
                                    <td> {user.dob}</td>
                                </tr>
                            )
                        }
                    </tbody>
                </table>
            </div>
        )
    }
}

export default Users;
