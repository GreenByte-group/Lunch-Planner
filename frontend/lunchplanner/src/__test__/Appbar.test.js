import React from 'react';
import Appbar from '../components/Appbar';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <Appbar/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});