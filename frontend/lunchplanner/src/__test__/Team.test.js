import React from 'react';
import Team from '../components/Team/Team';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <Team/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});