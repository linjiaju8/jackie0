import React from 'react';
import ReactDOM from 'react-dom';
import { Menu,Breadcrumb,Icon,Affix,Badge,Row,Col,Input,Button } from 'antd';
const SubMenu = Menu.SubMenu;

class AsideCollapseMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {current: 'home'};
    }

    getInitialState() {
        return {
            collapse: true
        };
    }

    onCollapseChange() {
        this.setState({
            collapse: !this.state.collapse,
        })
    }

    render() {
        const collapse = this.state.collapse;
        return (
            <div className={collapse ? "ant-layout-aside ant-layout-aside-collapse" : "ant-layout-aside"}>
                <aside className="ant-layout-sider">
                    <div className="ant-layout-logo">
                        <Badge count={0}>
                            <a href="#" className="head-example">
                                <img width="42" height="42"
                                     src="http://localhost:8080/resources/images/test-head-portrait.jpg"/>
                            </a>
                        </Badge>
                    </div>
                    <Menu mode="inline" theme="dark" defaultSelectedKeys={['user']}>
                        <Menu.Item key="user">
                            <Icon type="user"/><span className="nav-text">导航一</span>
                        </Menu.Item>
                        <Menu.Item key="setting">
                            <Icon type="setting"/><span className="nav-text">导航二</span>
                        </Menu.Item>
                        <Menu.Item key="laptop">
                            <Icon type="laptop"/><span className="nav-text">导航三</span>
                        </Menu.Item>
                        <Menu.Item key="notification">
                            <Icon type="notification"/><span className="nav-text">导航四</span>
                        </Menu.Item>
                        <Menu.Item key="folder">
                            <Icon type="folder"/><span className="nav-text">导航五</span>
                        </Menu.Item>
                    </Menu>
                    <div className="ant-aside-action" onClick={this.onCollapseChange.bind(this)}>
                        {collapse ? <Icon type="right"/> : <Icon type="left"/>}
                    </div>
                </aside>
                <div className="ant-layout-main">
                    <div className="ant-layout-header">
                        <Row>
                            <Col xs={24} sm={7} md={6} lg={4}>
                                <a id="app-logo" href="#">
                                    <img alt="logo"
                                         src="http://localhost:8080/resources/images/T1B9hfXcdvXXXXXXXX.svg"/>
                                    <span>后台管理系统</span>
                                </a>
                            </Col>
                            <Col xs={0} sm={17} md={18} lg={20}>
                                <div className="index-split-line">
                                    {/*我是分割线*/}
                                </div>
                                <div className="index-search-box">
                                    <Input addonAfter="搜索" placeholder="搜索"/>
                                </div>
                                <div className="index-top-info">
                                    欢迎，<a href="#">张三</a>
                                    &nbsp;&nbsp;&nbsp;&nbsp;
                                    <a href="#"><Icon type="logout"/>&nbsp;退出</a>
                                </div>
                            </Col>
                        </Row>
                    </div>
                    <div className="ant-layout-breadcrumb">
                        <Breadcrumb>
                            <Breadcrumb.Item>首页</Breadcrumb.Item>
                            <Breadcrumb.Item>应用列表</Breadcrumb.Item>
                            <Breadcrumb.Item>某应用</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="ant-layout-container">
                        <div className="ant-layout-content">
                            <div style={{ height: 600 }}>
                                内容区域
                            </div>
                        </div>
                    </div>
                    <div className="ant-layout-footer">
                        Ant Design 版权所有 © 2015 由蚂蚁金服体验技术部支持
                    </div>
                </div>
            </div>
        );
    }
}

class IndexContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <AsideCollapseMenu />
        );
    }
}

ReactDOM.render(<IndexContainer />, document.getElementById('indexContainer'));