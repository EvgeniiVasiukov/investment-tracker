import { env } from '../config/env'
import { ApiError, NetworkError } from "./ApiError.ts";
import type { HttpMethod, RequestOptions } from './types'
import { authStorage} from "@/features/auth/authStorage.ts";
import { authEvents} from "@/features/auth/authEvents.ts";

async function request<T>(
    method: HttpMethod,
    path: string,
    options: RequestOptions = {},
    body?: unknown,
): Promise<T> {
    const headers = new Headers(options.headers)
    const session = options.skipAuth ? null : authStorage.load()
    const token = options.skipAuth ? null : (options.token ?? session?.accessToken)

    if (token) {
        headers.set('Authorization', `Bearer ${token}`)
    }
    if (body !== undefined) {
        headers.set('Content-Type', 'application/json')
    }

    let response: Response

    try {
        response = await fetch(`${env.apiBaseUrl}${path}`, {
            method,
            headers,
            body: body === undefined ? undefined : JSON.stringify(body),
        })
    } catch (error) {
        if (error instanceof Error && error.name === 'AbortError') {
            throw error
        }

        throw new NetworkError()
    }

    if (!response.ok) {
        if (
            response.status == 401 && token && path !== '/auth/login'
        ) {
            const currentSession = authStorage.load()
            if (currentSession?.accessToken === token) {
                authEvents.notifyUnauthorized()
            }
        }
        throw new ApiError(response.status, `HTTP request failed with status ${response.status}`,)
    }


    if (response.status === 204 || response.status === 205) {
        return undefined as T
    }
    return response.json() as Promise<T>
}

export const httpClient = {
    get<T>(path: string, options?: RequestOptions): Promise<T> {
        return request<T>('GET', path, options)
    },
    post<T>(
        path: string,
        body: unknown,
        options?: RequestOptions,
    ): Promise<T> {
        return request<T>('POST', path, options, body)
    },

    put<T>(
        path: string,
        body: unknown,
        options?: RequestOptions,
    ): Promise<T> {
        return request<T>('PUT', path, options, body)
    },

    patch<T>(
        path: string,
        body: unknown,
        options?: RequestOptions,
    ): Promise<T> {
        return request<T>('PATCH', path, options, body)
    },

    delete<T>(
        path: string,
        options?: RequestOptions,
    ): Promise<T> {
        return request<T>('DELETE', path, options)
    },
}