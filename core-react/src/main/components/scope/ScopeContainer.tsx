import { FunctionComponent } from "react";

const ScopeContainer: FunctionComponent<{}> = ({ children }) => {
    return (
        <div className="flex-initial mt-5 w-full md:w-1/2 mx-auto text-left max-h-56 overflow-auto">
            { children }
        </div>
    )
}

export default ScopeContainer