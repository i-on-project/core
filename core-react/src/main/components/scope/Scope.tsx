import { FunctionComponent } from "react";

interface ScopeProps {
    name: string,
    description: string
}

const Scope: FunctionComponent<ScopeProps> = ({ name, description }) => {
    return (
        <div className="w-3/4 mx-auto">
            <h3 className="text-xl">{ name }</h3>
            <p className="break-words">
                { description }
            </p>
        </div>
    )
}

export default Scope