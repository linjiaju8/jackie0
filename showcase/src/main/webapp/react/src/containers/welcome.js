import React from 'react';
import ReactDOM from 'react-dom';

class Welcome extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <h5>欢迎使用后台管理系统！</h5>
        );
    }
}

export default Welcome;