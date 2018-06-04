import React from 'react';
import CreateTeamScreen from '../components/Team/CreateTeamScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
        <CreateTeamScreen
        location={{search: ""}}
        />
        </MemoryRouter>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});