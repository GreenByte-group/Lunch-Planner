import React from 'react';
import TeamInvitationList from '../components/Team/TeamInvitationList';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <TeamInvitationList/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});