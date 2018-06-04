import React from 'react';
import TeamScreen from '../components/Team/TeamScreen';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <TeamScreen id={1}
                    isAdmin={true}
                    description='Tralala'
                    name='Test'
        />
    ).toJSON();
    expect(tree).toMatchSnapshot();
});