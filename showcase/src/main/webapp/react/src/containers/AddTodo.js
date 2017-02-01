/**
 * Created by jackie0 on 2016/12/30.
 * redux官网TODO示例：搭配recat
 */
import React from 'react'
import { connect } from 'react-redux'
import { addTodo } from '../actions/actions'

let AddTodo = ({ dispatch }) => {
    let input;

    return (
        <div>
            <form onSubmit={e => {
                e.preventDefault();
                if (!input.value.trim()) {
                  return
                }
                dispatch(addTodo(input.value));
                input.value = ''
             }}>
                <input ref={node => {
                  input = node
                }}/>
                <button type="submit">
                    Add Todo
                </button>
            </form>
        </div>
    )
};
AddTodo = connect()(AddTodo);

export default AddTodo