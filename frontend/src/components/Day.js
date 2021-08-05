import React from 'react';
import DayProperties from '../components/DayProperties';
import { DateTime } from 'luxon';

class Day extends React.Component {
    constructor(props) {
        super(props);
        this.select = this.select.bind(this);
        this.chooseClass = this.chooseClass.bind(this);
        this.state = {
            selected: false,
        }
    }



    select() {
        this.setState({selected: true})
    }

    chooseClass() {
        let className = 'day';
        if (this.props.isToday) { className += ' today' };
        if (this.props.currMonth) { className += ' this-month-day' } else { className += ' other-day'};
        if (this.props.isSelected) { className += ' selected-day'} else { className += ' non-selected-day'};
        return className;
    }

    render() {
        const className = this.chooseClass();
        return( 
        <div className="day-holder">
            <div onClick={this.props.isSelected ? this.props.onClick.unselect : this.props.onClick.select}
            className={className}>
                <p>{this.props.day.day}</p>
            </div>
            {this.props.isSelected ? <DayProperties
             addDeadlineInCalendar={this.props.addDeadlineInCalendar}
              addEventInCalendar={this.props.addEventInCalendar}
               day={this.props.day}/> : null}
        </div>
        )
    }
}





  

export default Day;