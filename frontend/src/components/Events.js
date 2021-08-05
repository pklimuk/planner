import React from 'react';
import EventsService from '../services/EventsService';
import AddNewEvent from './AddNewEvent';
import { DateTime } from 'luxon';


class Events extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            events: [],
            addNew: false,
            edit: false,
            edited: {},
        }

        this.handleDeleteClick = this.handleDeleteClick.bind(this);
        this.handleEditClick = this.handleEditClick.bind(this);
        this.handleAddClick = this.handleAddClick.bind(this);
        this.refreshData = this.refreshData.bind(this);
        this.removeForms = this.removeForms.bind(this);
    }

    componentDidMount() {
        this.refreshData();
    }

    handleEditClick(event) {
        const eventToEdit = event;
        this.setState({
            addNew: false,
            edit: true,
            edited: eventToEdit,
        });
    }

    handleDeleteClick(event) {
        const eventToDelete = event;
        EventsService.deleteEvent(eventToDelete).then(() => {
            this.refreshData();
        });
    }

    async refreshData() {
        const getEventsRes = await EventsService.getEvents();
        const events = getEventsRes.data;

        if (!events.map(event => event.title).includes(this.state.edited.title)) {
            this.setState({
                events: events,
                edit: false,
                edited: {}
            });
        } else {
            this.setState({
                events: events,
            })
        }
    }

    handleAddClick() {
        this.setState({
            addNew: !this.state.addNew,
            edit: false,
        })
    }

    formatDate(isoDate) {
        return DateTime.fromISO(isoDate).toLocaleString({ weekday: 'short', day: '2-digit', month: 'long', hour: '2-digit', minute: '2-digit', hour12: false });
    }

    removeForms() {
        this.setState({
            addNew: false,
            edit: false,
            edited: {}
        });
    }

    render() { 
        return (
            <div className="events">
                <div className="events-list">
                    <h1>my events</h1>
                    {
                        this.state.events.length > 0 ? 
                        <table>
                        <colgroup>
                            <col className="deadline-event-title" />
                            <col className="deadline-event-time" />
                            <col className="deadline-event-time" />
                            <col className="deadline-event-to-do" />
                            <col className="deadline-event-groups" />
                            <col className="deadline-event-edit" />
                            <col className="deadline-event-delete" />
                        </colgroup>
                        <thead>
                            <th>event</th>
                            <th>start</th>
                            <th>end</th>
                            <th>description</th>
                            <th>groups</th>
                            <th></th>
                            <th></th>
                        </thead>
                        <tbody>
                            {
                                this.state.events.map(
                                    event =>
                                    <tr>
                                        <td>{event.title}</td>
                                        <td>{this.formatDate(event.start)}</td>
                                        <td>{this.formatDate(event.end)}</td>
                                        <td>{event.description}</td>
                                        <td>{event.groups.map(group => group.group_name).join(',')}</td>
                                        <td><button className="edit" onClick={() => this.handleEditClick(event)}>Edit</button></td>
                                        <td><button className="delete" onClick={() => this.handleDeleteClick(event)}>Delete</button></td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table> :
                        <p className="no-data">Click below to add an event</p>
                    }
                    <button className="plus" onClick={() => this.handleAddClick()}>+</button>
                </div>
                {this.state.addNew ? 
                <AddNewEvent
                isNew={this.state.addNew}
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms} /> : null}
                {this.state.edit ? 
                <AddNewEvent
                key={this.state.edited.title}
                isNew={this.state.addNew}
                edited={this.state.edited}
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms} /> : null}
            </div>
        );
    }
}
 
export default Events;