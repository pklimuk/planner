import React from 'react';
import GroupsService from '../services/GroupsService';
import AddNewGroup from './AddNewGroup';


class Groups extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            groups: [],
            addNew: false,
            edit: false,
            edited: {},
        }

        this.handleAddClick = this.handleAddClick.bind(this);
        this.handleEditClick = this.handleEditClick.bind(this);
        this.refreshData = this.refreshData.bind(this);
        this.removeForms = this.removeForms.bind(this);
    }


    componentDidMount() {
        this.refreshData();
    }

    handleAddClick() {
        this.setState({
            addNew: !this.state.addNew,
            edit: false,
        })
    }

    handleEditClick(group) {
        const groupToEdit = group;
        this.edited = groupToEdit;
        this.setState({
            addNew: false,
            edit: true,
            edited: groupToEdit,
        });
    }

    handleDeleteClick(group) {
        const groupToDelete = group;
        GroupsService.deleteGroup(groupToDelete).then(() => {
            this.refreshData();
        })
    }

    async refreshData() {
        const getGroupsRes = await GroupsService.getGroups();
        const groups = getGroupsRes.data;
        const getEvDedPromises = groups.map(group => GroupsService.getEventsDeadlines(group));
        const getEvDedResponses = await Promise.all(getEvDedPromises);
        const gotEvDed = getEvDedResponses.map(res => res.data);
        const groupsWithEvDed = groups.map(function(group,idx) {
            group.events = gotEvDed[idx].events;
            group.deadlines = gotEvDed[idx].deadlines;
            return group;
        });
        // Check if currently edited group still exists
        if (!groupsWithEvDed.map(group => group.group_name).includes(this.state.edited.group_name)) {
            this.setState({
                groups: groupsWithEvDed,
                edit: false,
                edited: {}
            });
        } else {
            this.setState({
                groups: groupsWithEvDed,
            })
        }
        
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
            <div className="groups">
                <div className="groups-list">
                    <h1>my groups</h1>
                    {
                        this.state.groups.length > 0 ? 
                        <table>
                        <colgroup>
                            <col className="deadline-event-title" />
                            <col className="deadline-event-to-do" />
                            <col className="deadline-event-groups" />
                            <col className="deadline-event-edit" />
                            <col className="deadline-event-delete" />
                        </colgroup>
                        <thead>
                            <th>group</th>
                            <th>description</th>
                            <th>events</th>
                            <th>deadlines</th>
                            <th></th>
                            <th></th>
                        </thead>
                        <tbody>
                            {
                                this.state.groups.map(
                                    group =>
                                    <tr>
                                        <td>{group.group_name}</td>
                                        <td>{group.group_description}</td>
                                        <td>{group.events.join(',')}</td>
                                        <td>{group.deadlines.join(',')}</td>
                                        <td><button className="edit" onClick={() => this.handleEditClick(group)}>Edit</button></td>
                                        <td><button className="delete" onClick={() => this.handleDeleteClick(group)}>Delete</button></td>
                                    </tr>
                                )
                            }
                        </tbody>
                    </table> :
                    <p className="no-data">Click below to add a group</p>
                    }
                    <button className="plus" onClick={() => this.handleAddClick()}>+</button>
                </div>
                {this.state.addNew ? 
                <AddNewGroup isNew={this.state.addNew} 
                name={this.state.addNew ? '' : this.state.edited.group_name}
                description={this.state.addNew ? '' : this.state.edited.group_description}
                edited={this.state.edit ? this.state.edited : null} 
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms} /> : null }
                {this.state.edit ? 
                <AddNewGroup key={this.state.edited.group_name} isNew={this.state.addNew} 
                name={this.state.addNew ? '' : this.state.edited.group_name}
                description={this.state.addNew ? '' : this.state.edited.group_description}
                edited={this.state.edit ? this.state.edited : null} 
                refreshAfterAdding={this.refreshData}
                removeFormAfterAdding={this.removeForms}  /> : null}
            </div>
        );
    }
}
 
export default Groups;