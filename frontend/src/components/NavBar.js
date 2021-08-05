import React from 'react';

import { Link } from 'react-router-dom';


class NavBar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
    if (this.props.loggedIn) {
        return (
            <nav>
                <ul className="links">
                  <li><Link to="/home"><p>home</p></Link></li>
                    <li><Link to="/profile"><p>profile</p></Link></li>
                    <li><Link to="/calendar"><p>calendar</p></Link></li>
                    <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        Activities
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <li><Link to="/events"><p>events</p></Link></li>
                        <li><Link to="/deadlines"><p>deadlines</p></Link></li>
                        <li><Link to="/groups"><p>groups</p></Link></li>
                    </ul>
                    </li>
                </ul>
            </nav>
        );
    } else {
        return (
            <nav>
                <ul className="links">
                    <li><Link to="/about"><p>about</p></Link></li>
                    <li><Link to="/log-in"><p>log in</p></Link></li>
                    <li><Link to="/usercreate"><p>create user</p></Link></li>
                </ul>
            </nav>
        )
    }
    }
}

export default NavBar;