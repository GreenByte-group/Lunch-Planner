import React from 'react';
import Search from '../components/Search';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <Search/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});