import { FunctionComponent } from "react"

interface BoxPageHeaderProps {
    className?: string
}

const BoxPageHeader: FunctionComponent<BoxPageHeaderProps> = ({ className, children }) => {
    return (
        <div className={`flex-initial ${className}`}>
            { children }
        </div>
    )
}

export default BoxPageHeader