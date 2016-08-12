import React from 'react';
import ReactDOM from 'react-dom';
import { Affix, Menu, Icon } from 'antd';
const SubMenu = Menu.SubMenu;
const MenuItemGroup = Menu.ItemGroup;

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
                            <a href="http://localhost:8080/showcase/react-showcase/react-helloworld.html" target="_blank" >Hello World!</a>
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

class IndexContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <TopMenu />
        );
    }
}

ReactDOM.render(<IndexContainer />, document.getElementById('indexContainer'));