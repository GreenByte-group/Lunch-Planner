import React from 'react';
import LocationScreen from '../components/LocationScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
        <LocationScreen/>
        </MemoryRouter>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});