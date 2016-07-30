/**
 * Created by jackie0 on 2016/7/29.
 * React的组件演示
 */

/*
 到目前为止，我们已经在源代码里面直接插入了评论数据。让我们将这些评论数据抽出来，放在一个 JSON 格式的变量中，
 然后将这个 JSON 数据渲染到评论列表。到最后，数据将会来自服务器，但是现在，直接写在源代码中
 */
var data = [
    {author: "jackie0", text: "这是第一个本地组件。"},
    {author: "jackie1", text: "这是另一个本地组件。"}
];

var CommentBox = React.createClass({displayName: "CommentBox",

    /*
     * 把服务器获取数据代码分离，在componentDidMount方法中通过定时器轮询，定时刷新数据
     */
    loadCommentsFromServer: function () {
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            type: 'GET',
            cache: false,
            success: function (data) {
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },

    /*
     * 我们需要从子组件传数据到它的父组件。我们在父组件的 render 方法中这样做：传递一个新的回调函数（ handleCommentSubmit ）到子组件，
     * 绑定它到子组件的 onCommentSubmit 事件上。无论事件什么时候触发，回调函数都会被调用
     */
    handleCommentSubmit: function (comment) {
        $.ajax({
            url: this.props.url,
            dataType: 'json',
            type: 'POST',
            data: comment,
            success: function (data) {
                this.setState({data: data});
            }.bind(this),
            error: function (xhr, status, err) {
                console.error(this.props.url, status, err.toString());
            }.bind(this)
        });
    },

    /*
     * 到目前为止，每一个组件都根据自己的 props 渲染了自己一次。 props 是不可变的：它们从父组件传递过来，“属于”父组件。
     * 为了实现交互，我们给组件引入了可变的 state 。this.state 是组件私有的，可以通过调用 this.setState() 来改变它。当 state 更新之后，组件就会重新渲染自己。
     * render() 方法依赖于 this.props 和 this.state ，框架会确保渲染出来的 UI 界面总是与输入（ this.props 和 this.state ）保持一致。
     * 当服务器拿到评论数据的时候，我们将会用已知的评论数据改变评论。让我们给 CommentBox 组件添加一个评论数组作为它的 state
     * getInitialState() 在组件的生命周期中仅执行一次，用于设置组件的初始化 state 。
     * */
    getInitialState: function () {
        return {data: []};
    },

    /*
     * 这里， componentDidMount 是一个组件渲染的时候被 React 自动调用的方法。动态更新界面的关键点就是调用 this.setState() 。
     * 我们用新的从服务器拿到的评论数组来替换掉老的评论数组，然后 UI 自动更新。有了这种反应机制，实现实时更新就仅需要一小点改动。
     * 在这里我们使用简单的轮询，但是你也可以很容易地改为使用 WebSockets 或者其他技术。
     */
    componentDidMount: function () {
        this.loadCommentsFromServer();
        setInterval(this.loadCommentsFromServer, this.props.pollInterval);
    },

    render: function () {
        return (
            React.createElement("div", {className: "commentBox"}, 
                React.createElement("h1", null, "评论列表"), 

                /*引入子组件*/
                React.createElement(CommentList, {
                    data: this.state.data}), " ", /*我们需要用一种模块化的方式将数据传入到 CommentList 。修改 CommentBox 和 React.render() 方法，通过 props 传递数据到 CommentList*/
                React.createElement(CommentForm, {onCommentSubmit: this.handleCommentSubmit})
            )
        );
    }
});

var CommentList = React.createClass({displayName: "CommentList",
    render: function () {
        /*动态地渲染评论*/
        var commentNodes = this.props.data.map(function (comment) {
            return (
                React.createElement(Comment, {author: comment.author}, 
                    comment.text
                )
            );
        });
        return (
            React.createElement("div", {className: "commentList"}, 
                /*从父组件给子组件Comment传数据author*/
                commentNodes
            )
        );
    }
});

var CommentForm = React.createClass({displayName: "CommentForm",
    handleSubmit: function (e) {
        e.preventDefault(); // 在事件回调中调用 preventDefault() 来避免浏览器默认地提交表单。
        var author = this.refs.author.value.trim(); // 我们利用 ref 属性给子组件命名，通过 this.refs 引用 DOM 节点。
        var text = this.refs.text.value.trim();
        if (!text || !author) {
            alert("请输入姓名或者评论！");
            return;
        }
        this.props.onCommentSubmit({author: author, text: text});
        this.refs.author.value = '';
        this.refs.text.value = '';
    },
    render: function () {
        return (
            React.createElement("form", {className: "commentForm", onSubmit: this.handleSubmit}, 
                React.createElement("input", {type: "text", ref: "author", placeholder: "输入您的昵称"}), 
                React.createElement("input", {type: "text", ref: "text", placeholder: "输入评论..."}), 
                React.createElement("input", {type: "submit", value: "提交"})
            )
        );
    }
});

/**
 * 创建 Comment 组件，该组件依赖于从父级传入的数据。从父组件传入的数据会做为子组件的 属性（ property ） ，
 * 这些 属性（ properties ） 可以通过 this.props 访问到。使用属性（ props ），
 * 我们就可以读到从 CommentList 传到 Comment 的数据，然后渲染一些标记。
 */
var Comment = React.createClass({displayName: "Comment",

    /*这是一个特殊的 API，故意让插入原始的 HTML 变得困难，但是对于 marked ，我们将利用这个后门。
     记住： 使用这个功能，你的代码就要依赖于 marked 的安全性。在本场景中，我们传入 sanitize: true ，告诉 marked 转义掉评论文本中的 HTML 标签而不是直接原封不动地返回这些标签。*/
    rawMarkup: function () {
        var rawMarkup = marked(this.props.children.toString(), {sanitize: true});
        return {__html: rawMarkup};
    },

    render: function () {
        return (
            React.createElement("div", {className: "comment"}, 
                React.createElement("h2", {className: "commentAuthor"}, 
                    this.props.author
                ), 
                /*使用 Markdown 格式的评论过的文本
                 这里我们唯一需要做的就是调用 marked 库。我们需要把 this.props.children 从 React 的包裹文本转换成 marked 能处理的原始的字符串，所以我们显示地调用了 toString() 。
                 但是这里有一个问题！我们渲染的评论内容在浏览器里面看起来像这样：“<p>This is <em>another</em> comment</p>”。我们希望这些标签能够真正地渲染成 HTML 。
                 那是 React 在保护你免受 XSS 攻击。这里有一种方法解决这个问题，但是框架会警告你别使用这种方法
                 */
                React.createElement("span", {dangerouslySetInnerHTML: this.rawMarkup()})
            )
        );
    }
});

// 获取本地数据
//ReactDOM.render(<CommentBox data={data} pollInterval={2000}/>, document.getElementById('content'));

// 从服务器获取数据
ReactDOM.render(React.createElement(CommentBox, {url: "http://localhost:8080/showcase/react/comment", 
                            pollInterval: 2000}), document.getElementById('content'));

/*
 * 注意，原生 HTML 元素名以小写字母开头，而自定义的 React 类名以大写字母开头。
 * */

// 参考http://reactjs.cn/react/docs/tutorial.html