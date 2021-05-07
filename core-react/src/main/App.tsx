import React, { useEffect, useState } from 'react';
import logo from './logo.png'
import { useParams } from 'react-router';
import { StringParam, useQueryParam } from 'use-query-params';
import BoxPage from './components/boxPage/BoxPage';
import BoxPageHeader from './components/boxPage/BoxPageHeader';
import BoxPageItems from './components/boxPage/BoxPageItems';
import ScopeContainer from './components/scope/ScopeContainer';
import Scope from './components/scope/Scope'
import Form from './components/form/Form';
import FormInputContainer from './components/form/FormInputContainer';

const AUTH_REQUEST_ENDPOINT = 'http://localhost:8080/api/auth/request'

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

function App() {
    const [success, setSuccess] = useState<Boolean>(false)
    const [data, setData] = useState<AuthData | null>(null)
    const [error, setError] = useState<Error | null>(null)

    const { authReqId } = useParams<PathParams>()
    const [secret] = useQueryParam('secret', StringParam)

    useEffect(() => {
        fetchData(authReqId, secret)
            .then(data => setData(data))
            .catch(error => setError(error))
    }, [authReqId, secret])

    let message = 'Loading'
    let scopesHtml

    if (data) {
        message = `${data.client.client_name} is requesting the following scopes:`
        scopesHtml = data.scopes.map(scope =>
            <Scope key={scope.scope_id} name={scope.scope_name} description={scope.scope_description} />
        )
    } else if (error) {
        message = error.message
    } else if (success) {
        message = 'You\'re authorized! You may now close this browser tab.'
    }

    const onFormSubmit: React.FormEventHandler<HTMLFormElement> = async e => {
        try {
            e.preventDefault()
            const elem = document.getElementById('submitButton') as HTMLButtonElement
            if (data && secret) {
                elem.disabled = true
                await verifyRequest(data, secret)
                setData(null)
                setSuccess(true)
            }
        } catch (e) {
            setData(null)
            setError(new Error('An error has occurred! Please try again later.'))
        }
    }

    const loading = !data && !error && !success
    return (
        <BoxPage>
            <BoxPageHeader className={loading ? 'animate-pulse' : undefined}>
                <img className="w-32 mx-auto mb-5" src={logo} alt="i-on logo" />
                <h1 className="text-4xl">Authorize your Login</h1>
                <div className="w-1/2 mx-auto">
                    { message }
                </div>
            </BoxPageHeader>
            { data &&
                <BoxPageItems>
                    <ScopeContainer>
                        { scopesHtml }
                    </ScopeContainer>
                    <Form onSubmit={onFormSubmit} className="flex-initial mt-5 w-1/2 mx-auto">
                        <FormInputContainer>
                            <input type="checkbox" id="scopeCheck" className="mx-2" required />
                            <label htmlFor="scopeCheck">I confirm that these scopes are going to be granted to the client app</label>
                        </FormInputContainer>
                        <FormInputContainer>
                            <button type="submit" id="submitButton" className="px-10 py-3 bg-green-600 hover:bg-green-700 focus:bg-green-700 disabled:opacity-50 disabled:cursor-not-allowed shadow-md rounded-md duration-200">Authorize</button>
                        </FormInputContainer>
                    </Form>
                </BoxPageItems>
            }
        </BoxPage>
    );
}

const fetchData = async (authReqId: string, secretId: any): Promise<AuthData> => {
    if (typeof secretId !== 'string')
        throw new Error('Invalid Secret Key')

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

const fetchEndpoint = async (endpoint: string, init?: RequestInit): Promise<Response> => {
    const resp = await fetch(endpoint, init)
        .catch(_ => Promise.reject(new Error('Unexpected Error')))

    if (!resp.ok)
        throw new Error('Invalid Request')

    return resp
}

const parseJson = async <T,>(response: Response): Promise<T> => {
    const json = await response.json()
        .catch(_ => Promise.reject(new Error('Unexpected Error')))

    return json as T
}

export default App;
