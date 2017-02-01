/**
 * Created by jackie0 on 2016/12/30.
 * redux官网TODO示例：搭配recat
 */
import React, { PropTypes } from 'react'

const Todo = ({ onClick, completed, text }) => (
    <li
        onClick={onClick}
        style={{
      textDecoration: completed ? 'line-through' : 'none'
    }}
    >
        {text}
    </li>
);

Todo.propTypes = {
    onClick: PropTypes.func.isRequired,
    completed: PropTypes.bool.isRequired,
    text: PropTypes.string.isRequired
};

export default Todo
