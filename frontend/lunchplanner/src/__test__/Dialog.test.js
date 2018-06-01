import React from 'react';
import Dialog from '../components/Dialog';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <Dialog/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});