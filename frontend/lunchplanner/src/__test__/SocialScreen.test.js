import React from 'react';
import SocialScreen from '../components/SocialScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <SocialScreen/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});