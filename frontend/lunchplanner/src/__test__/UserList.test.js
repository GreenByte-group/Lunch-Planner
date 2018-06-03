import React from 'react';
import UserList from '../components/User/UserList';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <UserList/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});