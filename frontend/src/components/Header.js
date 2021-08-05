import React from 'react';
import NavBar from './NavBar';

class Header extends React.Component {
    
    constructor(props) {
        super(props);
    }

    render() {
        const logOutButton = this.props.loggedIn ? <button className="logout" onClick={() => this.props.logOut()}>Log out</button> : null;
        const deleteButton = this.props.loggedIn ? <button className="delete" onClick={() => this.props.deleteUser()}>Delete</button> : null;
        return (
            <header>
                <i className="far fa-calendar-alt"></i>
                <NavBar loggedIn={this.props.loggedIn}/>
                <div className="logout-delete-holder">
                {logOutButton}
                {deleteButton}
                </div>
            </header>
        );
    }
}


export default Header;