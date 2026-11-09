import { env } from '../config/env'
import type { HttpMethod, RequestOptions } from './types'

async function request<T>(
    method: HttpMethod,
    path: string,
    options: RequestOptions = {},
    body?: unknown,
): Promise<T> {
    const headers = new Headers(options.headers)

    if (options.token) {
        headers.set('Authorization', `Bearer ${options.token}`)
    }

    const response = await fetch(`${env.apiBaseUrl}${path}`, {
        method,
        headers,
        body: body === undefined ? undefined : JSON.stringify(body),
    })

    if (!response.ok) {
        throw new Error(`HTTP error: ${response.status}`)
    }
    if (body !== undefined) {
        headers.set('Content-Type', 'application/json')
    }

    return response.json() as Promise<T>
}

export const httpClient = {
    get<T>(path: string, options?: RequestOptions): Promise<T> {
        return request<T>('GET', path, options)
    },
}