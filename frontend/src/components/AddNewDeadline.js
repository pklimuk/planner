import React from 'react';
import DeadlinesService from '../services/DeadlinesService';
import GroupsService from '../services/GroupsService';


class AddNewDeadline extends React.Component {



    constructor(props) {
        super(props);

        this.isNew = this.props.isNew;
        this.inCalendar = this.props.inCalendar;
    
        this.state = {
            title: "",
            deadline_time: "",
            description: "",
            groups: [],
        }


        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.pushToGroups = this.pushToGroups.bind(this);
        this.deleteFromGroups = this.deleteFromGroups.bind(this);
    }

    componentDidMount() {
        if (!this.isNew) {
            this.setState({
                title: this.props.edited.title,
                deadline_time: this.props.edited.deadline_time,
                description: this.props.edited.description,
                groups: this.props.edited.groups.map(group => group.group_name),
            })
        }
        if (this.inCalendar) {
            this.setState({
                deadline_time: this.props.selectedDay.set({hour: 12, minute: 0, second: 0}).toISO({ includeOffset: false }),
            })
        }
    }


    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value,
        });
    }


    async handleSubmit(event) {
        event.preventDefault();
        const getGroupsRes = await GroupsService.getGroups();
        const existGroups = getGroupsRes.data.map(group => group.group_name);
        const deadlineGroups = this.state.groups;
        const groupsToCreate = deadlineGroups.filter(group => !existGroups.includes(group));
        const createGroupPromises = groupsToCreate.map(group => GroupsService.postGroup({group_name: group, group_description: ''}));
        Promise.all(createGroupPromises).then(() => 
            this.isNew ? DeadlinesService.postDeadline(this.state) : DeadlinesService.editDeadline(this.props.edited, this.state)
        ).then(() => {this.props.removeFormAfterAdding(); this.props.refreshAfterAdding()});
    }

    pushToGroups(group) {
        const groups = this.state.groups;
        groups.push(group);
        this.setState({
            groups: groups,
        });
    }

    deleteFromGroups(group) {
        const oldGroups = this.state.groups;
        const newGroups = oldGroups.filter(groupName => groupName !== group);
        this.setState({
            groups: newGroups,
        })
    }

    isValid() {
        return this.state.title != '' && this.state.deadline_time != '';
    }

    render() {
        
        return (
            <div className="add-new">
            { this.isNew ? <h1>add new deadline</h1> : <h1>edit deadline</h1>}
            <form onSubmit={this.handleSubmit} className="custom-form" >
            <table>
            <tbody>
            <tr>
                <td><label htmlFor="title">title</label></td>
                <td><input id="title" name="title" type="text" value={this.state.title} onChange={this.handleInputChange}/></td>
            </tr>
            <tr>
                <td><label htmlFor="deadline_time">deadline</label></td>
                <td><input id="deadline_time" name="deadline_time" type="datetime-local" value={this.state.deadline_time} onChange={this.handleInputChange}/></td>
            </tr>
            <tr>
                <td><label htmlFor="description">description</label></td>
                <td><textarea id="description" name="description" rows="3" value={this.state.description} onChange={this.handleInputChange}></textarea></td>
            </tr>


            {this.state.groups.length > 0 ? 
            <tr>
                <td><label htmlFor="groups">groups</label></td>
                <td>

                {
                    this.state.groups.map(groupName => <div className="added-groups"><p>{groupName}</p><button className="delete" type="button" onClick={() => this.deleteFromGroups(groupName)}>Remove</button></div>)
                }
                
                </td>
            </tr> : null}


            <tr>
                <td><label htmlFor="group">add group</label></td>  
                <td><AddGroup groups={this.state.groups} pushToGroups={(group) => this.pushToGroups(group)} /></td>
            </tr>
            </tbody>
            </table>
            <input className="add-group" id="create" type="submit" value={ this.isNew ? "add" : "save"} disabled={!this.isValid()}/>
            </form>
            </div>
        )
    }
}


class AddGroup extends React.Component {

    constructor(props) {
        
        super(props);
        this.state = {
            adding: false,
            group: '',
        }
        this.plusGroup = this.plusGroup.bind(this);
        this.addGroup = this.addGroup.bind(this);
        this.submitGroup = this.submitGroup.bind(this);
        this.handleGroupInputChange = this.handleGroupInputChange.bind(this);
    }

    handleGroupInputChange(event) {
        const value = event.target.value;

        this.setState({
            group: value,
        });
    }

    plusGroup() {
        return(
            <div className="adding-group">
                <button className="plus" onClick={() => this.setState({adding: true})}>+</button>
            </div>
        )
    }

    isValid() {
        return this.state.group.trim() != '' ;
    }


    submitGroup() {
        this.props.pushToGroups(this.state.group);
        this.setState({
            adding: false,
            group: '',
        })
    }

    addGroup() {
        return(
            <div className="adding-group">
                <input id="group" name="group" type="text" value={this.state.group} onChange={this.handleGroupInputChange}></input>
                <button className="add" onClick={() => this.submitGroup()} disabled={!this.isValid()}>add</button>
            </div>
        )
    }

    render() {
        return this.state.adding ? this.addGroup() : this.plusGroup()
    }

}

export default AddNewDeadline;