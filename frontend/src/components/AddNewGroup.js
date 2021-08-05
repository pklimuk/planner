import React from 'react';
import GroupsService from '../services/GroupsService';


class AddNewGroup extends React.Component {
    constructor(props) {
        super(props);

        this.isNew = this.props.isNew;

        this.state = { 
            group_name: this.props.name,
            group_description: this.props.description,
        }

        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value,
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        if (this.isNew) {
            GroupsService.postGroup(this.state).then(() => {this.props.removeFormAfterAdding(); this.props.refreshAfterAdding()});
        } else {
            GroupsService.editGroup(this.props.edited, this.state).then(() => {this.props.removeFormAfterAdding(); this.props.refreshAfterAdding()});
        }
        
    }

    isValid() {
        return this.state.group_name != '';
    }


    render() { 
        return (
            <div className="add-new">
            {this.isNew ? <h1>add new group</h1> : <h1>edit group</h1>}
            <form onSubmit={this.handleSubmit} className="custom-form" >
            <table>
            <tbody>
            <tr>
                <td><label htmlFor="group_name">group</label></td>
                <td><input id="group_name" name="group_name" type="text" value={this.state.group_name} onChange={this.handleInputChange}/></td>
            </tr>
            <tr>
                <td><label htmlFor="group_description">description</label></td>
                <td><textarea id="group_description" name="group_description" rows="3" value={this.state.group_description} onChange={this.handleInputChange}></textarea></td>
            </tr>
            </tbody>
            </table>
            <input id="create" type="submit" value={this.isNew ? "add" : "save"} disabled={!this.isValid()}/>
            </form>
            </div>
        );
    }
}
 

export default AddNewGroup;