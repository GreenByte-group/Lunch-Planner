import React from 'react';
import FloatingActionButton from '../components/FloatingActionButton';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <FloatingActionButton/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});