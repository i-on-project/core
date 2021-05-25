import React from "react";
import ButtonProps from "./ButtonProps";

const SuccessButton = React.forwardRef<HTMLButtonElement, ButtonProps>((props, ref) => {
    return (
        <button 
        type={ props.type }
        ref={ ref }
        disabled={ props.disabled } 
        className="px-10 py-3 bg-green-600 hover:bg-green-700 focus:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed shadow-md rounded-md duration-200">
            { props.content }
        </button>
    )    
})

export default SuccessButton