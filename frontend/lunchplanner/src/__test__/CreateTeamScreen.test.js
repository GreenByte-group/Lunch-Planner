import React from 'react';
import CreateTeamScreen from '../components/Team/CreateTeamScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <CreateTeamScreen
        location={{search: ""}}
        />
    ).toJSON();
    expect(tree).toMatchSnapshot();
});