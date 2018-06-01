import React from 'react';
import Event from "../components/Event/Event";
import {MemoryRouter} from "react-router-dom";
import renderer from 'react-test-renderer';

test('Event with dummy data', () => {
    const event = renderer.create(
        <MemoryRouter>
            <Event background='#FFFFFF'
                   id={1}
                   name='event'
                   description='description'
                   date={new Date('2018-12-17T03:24:00')}
                   people={[{userName: 'name', answer: 0, admin: true}]}
                   location='location'
            />
        </MemoryRouter>,
    );

    expect(event).toMatchSnapshot();
});

