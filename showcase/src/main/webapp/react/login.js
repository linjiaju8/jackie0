/**
 * Created by jackie0 on 2016/8/31.
 */
import React from 'react';
import ReactDOM from 'react-dom';
import { Row, Col, Card, Button, Form, Input, Radio, Tooltip, Icon, Checkbox } from 'antd';
const FormItem = Form.Item;


class LoginForm extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    handleSubmit(e) {
        e.preventDefault();
        console.log('收到表单值：', this.props.form.getFieldsValue());
    }

    render() {
        return (
            <Form horizontal onSubmit={this.handleSubmit.bind(this)}>
                <FormItem>
                    <Input size="large" addonBefore={<Icon type="user" />} type="text" id="userName" name="userName"
                           placeholder="请输入用户名"/>
                </FormItem>
                <FormItem>
                    <Input size="large" addonBefore={<Icon type="lock" />} type="password" id="password" name="password"
                           placeholder="请输入密码"/>
                </FormItem>
                <FormItem wrapperCol={{ span: 16, offset: 6 }} style={{ marginTop: 24 }}>
                    <Button type="primary" htmlType="submit">确定</Button>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a href="#">还没有帐号？点击注册！</a>
                </FormItem>
            </Form>
        );
    }
}

LoginForm = Form.create()(LoginForm);

class LoginContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {};
    }

    render() {
        return (
            <div style={{marginTop: 50 + 'px'}}>
                <Row>
                    <Col span={8}/>
                    <Col span={8}>
                        <Card title="登录" style={{ width: 450 }}>
                            <LoginForm />
                        </Card>
                    </Col>
                    <Col span={8}/>
                </Row>
            </div>
        );
    }
}

ReactDOM.render(<LoginContainer />, document.getElementById('loginContainer'));