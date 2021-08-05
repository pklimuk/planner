import React from 'react';
import DeadlinesService from '../services/DeadlinesService';
import AddNewDeadline from './AddNewDeadline';
import { DateTime } from 'luxon';


class Deadlines extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            deadlines: [],
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

    handleEditClick(deadline) {
        const deadlineToEdit = deadline;
        this.setState({
            addNew: false,
            edit: true,
            edited: deadlineToEdit,
        });
    }

    handleDeleteClick(deadline) {
        const deadlineToDelete = deadline;
        DeadlinesService.deleteDeadline(deadlineToDelete).then(() => {
            this.refreshData();
        });
    }

    async refreshData() {
        const getDeadlinesRes = await DeadlinesService.getDeadlines();
        const deadlines = getDeadlinesRes.data;

        if (!deadlines.map(deadline => deadline.title).includes(this.state.edited.title)) {
            this.setState({
                deadlines: deadlines,
                edit: false,
                edited: {}
            });
        } else {
            this.setState({
                deadlines: deadlines,
            })
        }
    }

    formatDate(isoDate) {
        return DateTime.fromISO(isoDate).toLocaleString({ weekday: 'short', day: '2-digit', month: 'long', hour: '2-digit', minute: '2-digit', hour12: false });
    }

    handleAddClick() {
        this.setState({
            addNew: !this.state.addNew,
            edit: false,
        });
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
            <div className="deadlines">
                <div className="deadlines-list">
                    <h1>my deadlines</h1>
                    {
                        this.state.deadlines.length > 0 ? 
                        <table>
                        <colgroup>
                            <col className="deadline-event-title" />
                            <col className="deadline-event-time" />
                            <col className="deadline-event-to-do" />
                            <col className="deadline-event-groups" />
                            <col className="deadline-event-edit" />
                            <col className="deadline-event-delete" />
                        </colgroup>
                        <thead>
                            <th>title</th>
                            <th>deadline</th>
                            <th>to do</th>
                            <th>groups</th>
                            <th></th>
                            <th></th>
                        </thead>
                        <tbody>
                            {
                                this.state.deadlines.map(
                                    deadline =>
                                    <tr>
                                        <td>{deadline.title}</td>
                                        <td>{this.formatDate(deadline.deadline_time)}</td>
                                        <td>{deadline.description}</td>
                                        <td>{deadline.groups.map(group => group.group_name).join(',')}</td>
                                        <td><button className="edit" onClick={() => this.handleEditClick(deadline)}>Edit</button></td>
                                        <td><button className="delete" onClick={() => this.handleDeleteClick(deadline)}>Delete</button></td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table> :
                    <p className="no-data">Click below to add an deadline</p>
                    }
                    <button className="plus" onClick={() => this.handleAddClick()}>+</button>
                </div>
                {this.state.addNew ? 
                <AddNewDeadline
                isNew={this.state.addNew}
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms} /> : null}
                {this.state.edit ? 
                <AddNewDeadline
                key={this.state.edited.title}
                isNew={this.state.addNew}
                edited={this.state.edited}
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms} /> : null}
            </div>
        );
    }
}
 
export default Deadlines;