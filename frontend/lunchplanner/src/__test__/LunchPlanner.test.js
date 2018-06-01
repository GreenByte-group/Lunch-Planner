import React from 'react';
import LunchPlanner from '../components/LunchPlanner';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
        <LunchPlanner/>
        </MemoryRouter>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});