import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
import reportWebVitals from './reportWebVitals';
import VerifyAuth from './main/VerifyAuth';
import { QueryParamProvider } from 'use-query-params';
import NotFound from './main/NotFound';

ReactDOM.render(
    <React.StrictMode>
        <Router>
            <QueryParamProvider ReactRouterRoute={Route}>
                <Switch>
                    <Route path="/auth/request/:authReqId/verify" component={VerifyAuth}/>
                    <Route path="*" component={NotFound} />
                </Switch>
            </QueryParamProvider>
        </Router>
    </React.StrictMode>,
    document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
