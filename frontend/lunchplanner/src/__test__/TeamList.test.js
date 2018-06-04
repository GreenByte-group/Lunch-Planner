import React from 'react';
import TeamList from '../components/Team/TeamList';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
        <TeamList/>
        </MemoryRouter>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});