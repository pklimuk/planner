import React from 'react';
import Header from './components/Header';
import Calendar from './components/Calendar'
import UserCreate from './components/UserCreate';
import Users from './components/Users';
import UserService from './services/UserService';
import Login from './components/Login';
import HomePage from './components/HomePage';
import Auth from './services/security/auth';
import './App.css';
import {BrowserRouter as Router, Switch, Route, Link, Redirect} from "react-router-dom";
import CreateLoginPassword from './components/CreateLoginPassword';
import Deadlines from './components/Deadlines';
import Groups from './components/Groups';
import Events from './components/Events';
import UserProfile from './components/UserProfile';

class App extends React.Component {
  
  constructor(props) {
    super(props);

    this.state = {
      username: '',
      password: '',
      isLoggedIn: null,
    }

    this.loggedInApp = this.loggedInApp.bind(this);
    //this.unloggedInApp = this.unloggedInApp(this);
  }

  Username = '';

  componentDidMount() {
    if  (Auth.loggedIn()) {
      this.setState({
        isLoggedIn: true,
        username: localStorage.getItem('username'),
      });
    } else {
      this.setState({
        isLoggedIn: false,
      });
    }
  }

  loggedInApp() {
    return(
      <div className="App">
          <Header loggedIn={this.state.isLoggedIn} logOut={() => {Auth.logOut(); this.componentDidMount()}}  deleteUser={() => {UserService.deleteUser(); Auth.logOut(); this.componentDidMount()}} />
          <Route path="/home" render={(props) => (
            <HomePage {...props} username={this.state.username}/>
          )} />
          <Route path="/profile" component={UserProfile} />
          <Route path="/calendar" component={Calendar} />
          <Route path="/users" component={Users} />
          <Route path="/events" component={Events} />
          <Route path="/deadlines" component={Deadlines} />
          <Route path="/groups" component={Groups} />
          <Redirect to="/home"/>
      </div>
    )
  }

  unloggedInApp() {
    return(
      <div className="App">
        <Header loggedIn={this.state.isLoggedIn} />
        <Route path="/about" render={(props) => (
          <HomePage {...props} username="Stranger" />
        )} />
        <Route path="/log-in" render={(props) => (
          <Login {...props} getUsername={(username) => localStorage.setItem('username', username)} refreshAsLoggedIn={() => this.componentDidMount()} />
          )} />
        <Route exact path="/usercreate" component={UserCreate} />
        <Route path="/usercreate/login-password" component={CreateLoginPassword} />
        <Redirect to="/about"/>
      </div>
    )
  }


  render() {
    const isLoggedIn = this.state.isLoggedIn;
    return ( 
    <Router>
      {isLoggedIn ? this.loggedInApp() : this.unloggedInApp()}
    </Router>
    );
  }
}
 


export default App;
