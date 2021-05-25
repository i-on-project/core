export interface CancellableRequest {
    isCancelled(): boolean
    request(): Promise<Response>
    cancel(): void
}

export const newCancellableRequest = (endpoint: string, init: RequestInit = {}): CancellableRequest => {
    const abortController = new AbortController()
    init.signal = abortController.signal

    let futureResponse: Promise<Response> | undefined
    let cancelled = false

    return {
        isCancelled: () => cancelled,
        request: () => {
            if (futureResponse)
                return futureResponse

            const request = fetchEndpoint(endpoint, init)
            futureResponse = request.then(resp => {
                if (cancelled)
                    throw new Error('The request has been cancelled')

                return resp
            })

            return futureResponse
        },
        cancel: () => {
            if (!cancelled && futureResponse) {
                cancelled = true
                abortController.abort()
            }
        }
    }
}

const fetchEndpoint = async (endpoint: string, init?: RequestInit): Promise<Response> => {
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
