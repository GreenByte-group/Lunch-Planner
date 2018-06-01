import React from 'react';
import BottomNavigationBar from '../components/BottomNavigationBar';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <MemoryRouter>
            <BottomNavigationBar/>
        </MemoryRouter>

    ).toJSON();
    expect(tree).toMatchSnapshot();
});