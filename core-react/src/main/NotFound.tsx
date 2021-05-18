import BoxPage from "./components/boxPage/BoxPage"
import BoxPageHeader from "./components/boxPage/BoxPageHeader"

const NotFound = () => {
    return (
        <BoxPage>
            <BoxPageHeader title="Page Not Found" message="The page you were looking for wasn't found" />
        </BoxPage>
    )
}

export default NotFound