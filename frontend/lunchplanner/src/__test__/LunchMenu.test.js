import React from 'react';
import LunchMenu from '../components/LunchMenu';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
        <LunchMenu/>
        </MemoryRouter>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});