import React from 'react';
import TeamScreen from '../components/Team/TeamScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <TeamScreen/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});