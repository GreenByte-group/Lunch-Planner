import React from 'react';
import TextFieldEditing from '../components/editing/TextFieldEditing';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
    const tree = renderer.create(
        <TextFieldEditing/>
    ).toJSON();
    expect(tree).toMatchSnapshot();
});