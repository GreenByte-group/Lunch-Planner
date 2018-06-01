import React from 'react';
import EventContainer from '../components/EventContainer';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const wrapper = renderer.create(
        <MemoryRouter>
            <EventContainer/>
        </MemoryRouter>
    ).toJSON();

    expect(wrapper).toMatchSnapshot();
});