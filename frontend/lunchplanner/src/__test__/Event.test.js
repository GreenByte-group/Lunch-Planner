import React from 'react';
import Event from "../components/Event/Event";
import {StaticRouter} from "react-router-dom";
import renderer from 'react-test-renderer';
import { shallow, mount, render } from 'enzyme';
import * as enzyme from "enzyme";

test('Event with dummy data', () => {
    // Render a checkbox with label in the document
    const event = renderer.create(
        <StaticRouter>
            <Event background='#FFFFFF'
                   id={1}
                   name='event'
                   description='description'
                   date={new Date('2018-12-17T03:24:00')}
                   people={[{userName: 'name', answer: 0, admin: true}]}
                   location='location'
            />
        </StaticRouter>,
    );

    expect(event).toMatchSnapshot();
});

