import { FunctionComponent } from "react";

const FormInputContainer: FunctionComponent<{}> = ({ children }) => {
    return (
        <div className="mt-5">
            { children }
        </div>
    )
}

export default FormInputContainer