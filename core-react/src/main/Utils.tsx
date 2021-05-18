export const fetchEndpoint = async (endpoint: string, init?: RequestInit): Promise<Response> => {
    const resp = await fetch(endpoint, init)
        .catch(_ => Promise.reject(new Error('Unexpected Error')))

    if (!resp.ok)
        throw new Error('Invalid Request')

    return resp
}

export const parseJson = async <T,>(response: Response): Promise<T> => {
    const json = await response.json()
        .catch(_ => Promise.reject(new Error('Unexpected Error')))

    return json as T
}
