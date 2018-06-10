// index.js
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('root'));

try {
    registerServiceWorker();
} catch (e) {
    console.log('Register service worker failed: ', e);
}