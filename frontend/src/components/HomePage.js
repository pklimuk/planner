import React from 'react';


class HomePage extends React.Component {
    constructor(props) {
        super(props);
        this.state = {  }
    }
    render() { 
        return (
        <div className="home-page">
        <article>
            <h1>hi there, {this.props.username}!</h1>
            <p>You're now on a page of a Planner made by Stanislau Strankevich and Pavel Klimuk (best planner/calendar application in the world). Try it now! </p>
        </article>
        <i className="far fa-calendar-alt"></i>
        </div>
        );
    }
}

export default HomePage;
