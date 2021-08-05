import React, { useState, useEffect } from 'react';
import UserService from '../services/UserService';
import FormData from 'form-data';

const UserProfile = (props) => {
    const [profile, setProfile] = useState({});
    const [newProfile, setNewProfile] = useState({});
    const [edit, setEdit] = useState(false);
    const [changed, setChanged] = useState(false);
    const [uploadImg, setUploadImg] = useState(false);
    const [img, setImg] = useState({});

    useEffect(() => {
        UserService.getProfile().then((res) => {
            console.log(res.data);
            const data = res.data;
            const dataToEdit = {};
            for (let key of Object.keys(data)) {
                dataToEdit[`new_${key}`] = data[key];
            }
            setProfile(data);
            setNewProfile(dataToEdit);
            setChanged(false);
        });
    }, [changed]);


    const handleInputChange = (event) => {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        
        const changedValue = {[name]: value};

        if (!edit) {
            setEdit(true);
        }

        setNewProfile({
            ...newProfile,
            ...changedValue
        });
    }


    const handleSubmit = (event) => {
        event.preventDefault();
        UserService.changeProfile(newProfile).then(() => {
            setChanged(true);
            setEdit(false);
        });
    }


    const handleImageUpload = (event) => {
        event.preventDefault();
        var bodyFormData = new FormData();
        bodyFormData.append('profile_image', event.target.files[0]);
        console.log(Array.from(bodyFormData));
        setImg(bodyFormData);
    }


    const putImage = (event) => {
        event.preventDefault();
        console.log(img);
        UserService.changeProfileImage(img).then(() => {
            setChanged(true);
            setUploadImg(false);
        });
    }

    return (
        <>
            <div className="container">
                <div className="row">
                    <div className="col-sm d-flex flex-column align-items-center position-relative">
                        <img src={`data:image/jpeg;base64,${profile.profileImage}`}/>
                        <button type="button" className="btn btn-outline-primary position-absolute upload-image-button top-0" onClick={() => setUploadImg(true)}>Upload</button>
                        {uploadImg && <form onSubmit={putImage}>
                            <input
                        type='file'
                        id='upload'
                        name='upload'
                        accept='image/jpeg'
                        onChange={handleImageUpload} ></input>
                        <button className="btn btn-primary">Save</button>
                        </form>}
                    </div>
                    <div className="col-sm">
                        <h3>{`${profile.firstName} ${profile.lastName}`}</h3>
                        <h6 className="text-muted">{`${profile.email}`}</h6>
                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                            <label htmlFor="new_firstName" className="form-label">First name</label>
                            <input type="text" name="new_firstName" id="new_firstName" className="form-control" value={newProfile.new_firstName} onChange={handleInputChange}></input>
                            </div>
                            <div className="mb-3">
                            <label htmlFor="new_lastName" className="form-label">Last name</label>
                            <input type="text" name="new_lastName" id="new_lastName" className="form-control" value={newProfile.new_lastName} onChange={handleInputChange}></input>
                            </div>
                            <div className="mb-3">
                            <label htmlFor="new_dob" className="form-label">Date of birth</label>
                            <input type="date" name="new_dob" id="new_dob" className="form-control" value={newProfile.new_dob} onChange={handleInputChange}></input>
                            </div>
                            <div className="mb-3">
                            <label htmlFor="new_email" className="form-label">Email</label>
                            <input type="email" name="new_email" id="new_email" className="form-control" value={newProfile.new_email} onChange={handleInputChange}></input>
                            </div>
                            { edit && <button className="btn btn-primary">Save</button> }
                        </form>
                    </div>
                </div>
                
            </div>
        </>
    )
}

export default UserProfile;