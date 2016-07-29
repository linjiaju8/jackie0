/**
 * Created by jackie0 on 2016/7/29.
 * React的组件演示
 */

/*
 到目前为止，我们已经在源代码里面直接插入了评论数据。让我们将这些评论数据抽出来，放在一个 JSON 格式的变量中，
 然后将这个 JSON 数据渲染到评论列表。到最后，数据将会来自服务器，但是现在，直接写在源代码中
 */
var data = [
    {author: "jackie0", text: "这是第一个组件。"},
    {author: "jackie1", text: "这是另一个组件。"}
];

var CommentBox = React.createClass({displayName: "CommentBox",
    render: function () {
        return (
            React.createElement("div", {className: "commentBox"}, 
                React.createElement("h1", null, "Comments"), 

                /*引入子组件*/
                React.createElement(CommentList, {
                    data: this.props.data}), " ", /*我们需要用一种模块化的方式将数据传入到 CommentList 。修改 CommentBox 和 React.render() 方法，通过 props 传递数据到 CommentList*/
                React.createElement(CommentForm, null)
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
    render: function () {
        return (
            React.createElement("div", {className: "commentForm"}, 
                "你好！这是一个Form组件。"
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

// 获取本地数据 ReactDOM.render(<CommentBox data={data}/>, document.getElementById('content'));

// 从服务器获取数据
ReactDOM.render(React.createElement(CommentBox, {url: "/showcase/react/comment"}), document.getElementById('content'));

/*
 * 注意，原生 HTML 元素名以小写字母开头，而自定义的 React 类名以大写字母开头。
 * */

// 参考http://reactjs.cn/react/docs/tutorial.html