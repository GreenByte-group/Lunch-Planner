// index.js
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import 'bootstrap/dist/css/bootstrap.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
import Appbar from "./components/Appbar";
import FullWidthTabs from "./components/FullWidthTabs";
import BottomNavigationBar from "./components/BottomNavigationBar";

//ReactDOM.render(<App />, document.getElementById('root'));
ReactDOM.render(<Appbar/>, document.getElementById("nav"));
ReactDOM.render(<FullWidthTabs/>, document.getElementById("tab"));
ReactDOM.render(<BottomNavigationBar />, document.getElementById("bot"));

registerServiceWorker();