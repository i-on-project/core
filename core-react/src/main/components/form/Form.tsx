import React, { FunctionComponent } from "react"

export enum FormMethod {
    POST = "POST",
    GET = "GET"
}

interface FormProps {
    className?: string,
    method?: FormMethod,
    action?: string,
    onSubmit?: React.FormEventHandler<HTMLFormElement>
}

const Form: FunctionComponent<FormProps> = ({ className, method, action, onSubmit, children }) => {
    return (
        <form className={className} method={method} action={action} onSubmit={onSubmit}>
            { children }
        </form>
    )
}

export default Form