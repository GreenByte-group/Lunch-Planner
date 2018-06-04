import React from 'react';
import TeamInvitation from '../components/Team/TeamInvitation';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <TeamInvitation/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});