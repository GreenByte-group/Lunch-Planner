import React from 'react';
import Login from '../components/authentication/Login';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <Login/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});