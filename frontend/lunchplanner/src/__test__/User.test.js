import React from 'react';
import User from '../components/User/User';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <User/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});