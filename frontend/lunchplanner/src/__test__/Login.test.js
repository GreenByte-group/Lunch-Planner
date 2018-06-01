import React from 'react';
import Login from '../components/Login';
import renderer from 'react-test-renderer';
import {MemoryRouter} from "react-router-dom";

it ("renders correctly", ()=>{
   const tree = renderer.create(
       <MemoryRouter>
           <Login location={{state: "/"}} />
       </MemoryRouter>
   ).toJSON();

   expect(tree).toMatchSnapshot();
});