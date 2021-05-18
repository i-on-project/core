import { FunctionComponent } from "react"
import logo from '../../logo.png'

interface BoxPageHeaderProps {
    className?: string,
    title: string
    message?: string
}

const BoxPageHeader: FunctionComponent<BoxPageHeaderProps> = ({ className, title, message }) => {
    return (
        <div className={`flex-initial ${className}`}>
            <img className="w-32 mx-auto mb-5" src={logo} alt="i-on logo" />
            <h1 className="text-4xl">{ title }</h1>
            { message &&
                <div className="w-1/2 mx-auto">
                    { message }
                </div>
            }
        </div>
    )
}

export default BoxPageHeader