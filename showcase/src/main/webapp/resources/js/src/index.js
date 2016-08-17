import React from 'react';
import ReactDOM from 'react-dom';
import { Menu,Breadcrumb, Icon,Affix } from 'antd';
const SubMenu = Menu.SubMenu;

class TopMenu extends React.Component {
    constructor(props) {
        super(props);
        this.state = {current: 'home'};
    }

    handleClick(e) {
        console.log('click ', e);
        this.setState({
            current: e.key
        });
    }

    render() {
        return (
            <Affix>
                <Menu onClick={this.handleClick.bind(this)}
                      selectedKeys={[this.state.current]}
                      mode="horizontal"
                >
                    <Menu.Item key="home">
                        <a href="http://localhost:8080/showcase/"><Icon type="home"/>演示系统</a>
                    </Menu.Item>
                    <SubMenu title={<span><Icon type="appstore" />React</span>}>
                        <Menu.Item key="HelloWorld">
                            <a href="http://localhost:8080/showcase/react-showcase/react-helloworld.html"
                               target="_blank">Hello World!</a>
                        </Menu.Item>
                        <Menu.Item key="comment">
                            <a href="http://localhost:8080/showcase/react-showcase/react-comment.html" target="_blank">评论</a>
                        </Menu.Item>
                    </SubMenu>
                </Menu>
            </Affix>
        );
    }
}

const AsideCollapse = React.createClass({
    getInitialState() {
        return {
            collapse: true,
        };
    },
    onCollapseChange() {
        this.setState({
            collapse: !this.state.collapse,
        })
    },
    render() {
        const collapse = this.state.collapse;
        return (
            <div className="ant-layout-topaside">
                <div className="ant-layout-header">
                    <div className="ant-layout-wrapper">
                        <div className="ant-layout-logo"></div>
                        <Menu theme="dark" mode="horizontal"
                              defaultSelectedKeys={['2']} style={{lineHeight: '64px'}}>
                            <Menu.Item key="1">导航一</Menu.Item>
                            <Menu.Item key="2">导航二</Menu.Item>
                            <Menu.Item key="3">导航三</Menu.Item>
                        </Menu>
                    </div>
                </div>
                <div className="ant-layout-subheader">
                    <div className="ant-layout-wrapper">
                        <Menu mode="horizontal"
                              defaultSelectedKeys={['1']} style={{marginLeft: 124}}>
                            <Menu.Item key="1">二级导航</Menu.Item>
                            <Menu.Item key="2">二级导航</Menu.Item>
                            <Menu.Item key="3">二级导航</Menu.Item>
                        </Menu>
                    </div>
                </div>
                <div className="ant-layout-wrapper">
                    <div className="ant-layout-breadcrumb">
                        <Breadcrumb>
                            <Breadcrumb.Item>首页</Breadcrumb.Item>
                            <Breadcrumb.Item>应用列表</Breadcrumb.Item>
                            <Breadcrumb.Item>某应用</Breadcrumb.Item>
                        </Breadcrumb>
                    </div>
                    <div className="ant-layout-container">
                        <aside className="ant-layout-sider">
                            <Menu mode="inline" defaultSelectedKeys={['1']} defaultOpenKeys={['sub1']}>
                                <SubMenu key="sub1" title={<span><Icon type="user" />导航一</span>}>
                                    <Menu.Item key="1">选项1</Menu.Item>
                                    <Menu.Item key="2">选项2</Menu.Item>
                                    <Menu.Item key="3">选项3</Menu.Item>
                                    <Menu.Item key="4">选项4</Menu.Item>
                                </SubMenu>
                                <SubMenu key="sub2" title={<span><Icon type="laptop" />导航二</span>}>
                                    <Menu.Item key="5">选项5</Menu.Item>
                                    <Menu.Item key="6">选项6</Menu.Item>
                                    <Menu.Item key="7">选项7</Menu.Item>
                                    <Menu.Item key="8">选项8</Menu.Item>
                                </SubMenu>
                                <SubMenu key="sub3" title={<span><Icon type="notification" />导航三</span>}>
                                    <Menu.Item key="9">选项9</Menu.Item>
                                    <Menu.Item key="10">选项10</Menu.Item>
                                    <Menu.Item key="11">选项11</Menu.Item>
                                    <Menu.Item key="12">选项12</Menu.Item>
                                </SubMenu>
                            </Menu>
                        </aside>
                        <div className="ant-layout-content">
                            <div style={{ height: 240 }}>
                                <div style={{clear: 'both'}}>内容区域</div>
                            </div>
                        </div>
                    </div>
                    <div className="ant-layout-footer">
                        Ant Design 版权所有 © 2015 由蚂蚁金服体验技术部支持
                    </div>
                </div>
            </div>
        );
    },
});

class IndexContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <AsideCollapse />
        );
    }
}

ReactDOM.render(<IndexContainer />, document.getElementById('indexContainer'));