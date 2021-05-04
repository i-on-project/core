import React from 'react';
import { useParams } from 'react-router';

interface ParamTypes {
    authReqId: string
}

function App() {
    const { authReqId } = useParams<ParamTypes>()
    return (
        <div className="absolute w-full h-full">
            <div className="flex justify-center h-full">
                <div className="flex-shrink-0 self-center bg-gray-100 md:h-3/4 md:w-2/6 h-full w-full p-10 rounded-xl shadow-md">
                    hello world
                </div>
            </div>
        </div>
    );
}

export default App;
