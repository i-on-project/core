import { FunctionComponent } from "react"

const BoxPage: FunctionComponent<{}> = ({ children }) => {
    return (
        <div className="absolute w-full h-full bg-gray-900">
            <div className="flex justify-center items-center h-full">
                <div className="flex-shrink-0 bg-gray-800 text-gray-200 md:h-auto md:w-auto h-full w-full p-10 md:rounded-xl shadow-md text-center">
                    <div className="flex flex-col justify-center h-full">
                        { children }
                    </div>
                </div>
            </div>
        </div>
    )
}

export default BoxPage