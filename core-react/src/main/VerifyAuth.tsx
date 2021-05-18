import React, { useEffect, useReducer, useRef } from 'react';
import { useParams } from 'react-router';
import { StringParam, useQueryParam } from 'use-query-params';
import BoxPage from './components/boxPage/BoxPage';
import BoxPageHeader from './components/boxPage/BoxPageHeader';
import BoxPageItems from './components/boxPage/BoxPageItems';
import ScopeContainer from './components/scope/ScopeContainer';
import Scope from './components/scope/Scope'
import Form from './components/form/Form';
import FormInputContainer from './components/form/FormInputContainer';
import { fetchEndpoint, parseJson } from './Utils';
import SuccessButton from './components/button/SuccessButton';

const API_URI = process.env.NODE_ENV === 'production' ? process.env.REACT_APP_API_URI : 'http://localhost:8080/'
const AUTH_REQUEST_ENDPOINT = `${API_URI}api/auth/request`

interface PathParams {
    authReqId: string
}

interface AuthClient {
    client_id: string,
    client_name: string,
    client_url: string
}

interface AuthScope {
    scope_id: string,
    scope_name: string,
    scope_description: string
}

interface AuthData {
    auth_req_id: string,
    expires_on: Date,
    client: AuthClient,
    scopes: AuthScope[],
    verify_action: string
}

enum AuthStateType {
    LOADING,
    ERROR,
    NOT_COMPLETED,
    COMPLETED
}

interface AuthStateAction {
    type: AuthStateType,
    error?: Error,
    data?: AuthData
}

interface AuthState {
    type: AuthStateType,
    title: string,
    message: string
    scopesHtml?: JSX.Element[]
    data?: AuthData
}

const initialState: AuthState = {
    type: AuthStateType.LOADING,
    title: 'Loading Data',
    message: 'Please wait while we load your data'
}

const authStateReducer = (prevState: AuthState, action: AuthStateAction): AuthState => {
    // Loading -> Not_Completed transition
    if (prevState.type === AuthStateType.LOADING && action.type === AuthStateType.NOT_COMPLETED) {
        const data = action.data!
        const title = 'Authorize your Login'
        const message = `${data.client.client_name} is requesting the following scopes:`
        const scopesHtml = data.scopes.map(scope =>
            <Scope key={scope.scope_id} name={scope.scope_name} description={scope.scope_description} />
        )

        return {
            type: action.type,
            title,
            message,
            scopesHtml,
            data: action.data
        }
    }

    // Not_Completed -> Completed transition
    if (prevState.type === AuthStateType.NOT_COMPLETED && action.type === AuthStateType.COMPLETED) {
        const title = 'Authorization Successful'
        const message = 'You\'re authorized! You may now close this browser tab.'

        return {
            type: action.type,
            title,
            message
        }
    }

    // Loading -> Error and Not_Completed -> Error transitions
    if ((prevState.type === AuthStateType.LOADING || prevState.type === AuthStateType.NOT_COMPLETED) && action.type === AuthStateType.ERROR) {
        const title = 'An error has occurred'
        const message = action.error?.message || 'Unexpected Error'
        return {
            type: action.type,
            title,
            message
        }
    }

    return prevState
}

const VerifyAuth = () => {
    const { authReqId } = useParams<PathParams>()
    const [secret] = useQueryParam('secret', StringParam)

    const [state, dispatcher] = useReducer(authStateReducer, initialState)
    const submitButton = useRef<HTMLButtonElement>(null)

    useEffect(() => {
        fetchData(authReqId, secret)
            .then(data => dispatcher({ type: AuthStateType.NOT_COMPLETED, data }))
            .catch(error => dispatcher({ type: AuthStateType.ERROR, error }))
    }, [authReqId, secret])

    const onFormSubmit: React.FormEventHandler<HTMLFormElement> = async e => {
        try {
            e.preventDefault()
            console.log(submitButton.current)
            const elem = submitButton.current!
            if (state.type === AuthStateType.NOT_COMPLETED && secret) {
                elem.disabled = true
                await verifyRequest(state.data!, secret)
                dispatcher({ type: AuthStateType.COMPLETED })
            }
        } catch (e) {
            dispatcher({
                type: AuthStateType.ERROR,
                error: e
            })
        }
    }

    return (
        <BoxPage>
            <BoxPageHeader className={state.type === AuthStateType.LOADING ? 'animate-pulse' : undefined} title={state.title} message={state.message} />
            { state.type === AuthStateType.NOT_COMPLETED &&
                <BoxPageItems>
                    <ScopeContainer>
                        { state.scopesHtml }
                    </ScopeContainer>
                    <Form onSubmit={onFormSubmit} className="flex-initial mt-5 w-1/2 mx-auto">
                        <FormInputContainer>
                            <input type="checkbox" id="scopeCheck" className="mx-2" required />
                            <label htmlFor="scopeCheck">I confirm that these scopes are going to be granted to the client app</label>
                        </FormInputContainer>
                        <FormInputContainer>
                            <SuccessButton type="submit" content="Authorize" ref={submitButton} />
                        </FormInputContainer>
                    </Form>
                </BoxPageItems>
            }
        </BoxPage>
    );
}

const fetchData = async (authReqId: string, secretId: any): Promise<AuthData> => {
    if (typeof secretId !== 'string')
        throw new Error('Secret Key Not Found')

    const endpoint = `${AUTH_REQUEST_ENDPOINT}/${authReqId}`
    const response = await fetchEndpoint(endpoint)
    return parseJson(response)
}

const verifyRequest = (authData: AuthData, secret: string): Promise<any> => {
    return fetchEndpoint(authData.verify_action, {
        method: 'POST',
        body: JSON.stringify({
            auth_req_id: authData.auth_req_id,
            secret
        }),
        headers: {
            'Content-Type': 'application/json'
        }
    })
}

export default VerifyAuth;
