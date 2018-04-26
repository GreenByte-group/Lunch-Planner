// index.js
import React from 'react';
import ReactDom from 'react-dom';
import { Router } from 'react-router';
import routes from './routes';
import registerServiceWorker from "./registerServiceWorker";

//ReactDOM.render(<App />, document.getElementById('root'));
//ReactDOM.render(<Login />, document.getElementById('root'));
ReactDOM.render(<Registration />, document.getElementById('root'));
registerServiceWorker();