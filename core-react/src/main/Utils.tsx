export interface CancellableRequest {
    isCancelled(): boolean
    request(): Promise<Response>
    cancel(): void
}

export const newCancellableRequest = (endpoint: string, init: RequestInit = {}): CancellableRequest => {
    const abortController = new AbortController()
    const signal = abortController.signal
    init.signal = signal

    let futureResponse: Promise<Response> | undefined

    return {
        isCancelled: () => signal.aborted,
        request: () => {
            if (futureResponse)
                return futureResponse

            const request = fetchEndpoint(endpoint, init)
            futureResponse = request.then(resp => {
                if (signal.aborted)
                    throw new Error()

                return resp
            }).catch(err => {
                if (signal.aborted)
                    throw new Error('The request has been cancelled')

                throw err
            })

            return futureResponse
        },
        cancel: () => {
            if (!signal.aborted && futureResponse) {
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
