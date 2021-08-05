import React from 'react';
import { DateTime } from 'luxon';

class DayProperties extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            value: '+',
            show: false,
        };
        this.addText = this.addText.bind(this);
        this.removeText = this.removeText.bind(this);
        this.showOptions = this.showOptions.bind(this);
    }

    addText() {
        this.setState({
            value: 'add'
        })
    }

    removeText() {
        this.setState({
            value: '+'
        })
    }

    showOptions() {
        this.setState({
            show: true,
        })
    }

    render() {
        return(
            <div className="day-properties">
            <p>This is {this.props.day.day}th {this.props.day.monthLong} {this.props.day.year}</p>
            {
                this.state.show
                ? <div className="add-to-day-options">
                    <button onClick={this.props.addEventInCalendar} className="add">event</button>
                    <button onClick={this.props.addDeadlineInCalendar} className="add">deadline</button>
                    </div>
                : null
            }
            <button onClick={this.showOptions} onMouseMove={this.addText} onMouseLeave={this.removeText} className="plus-calendar">{this.state.value}</button>
          </div>
        )
    }
}

export default DayProperties;