import { env } from '../config/env'
import type { RequestOptions } from './types'

export const httpClient = {
    async get<T>(path: string, options: RequestOptions = {}): Promise<T> {
        const headers = new Headers(options.headers)

        if (options.token) {
            headers.set('Authorization', `Bearer ${options.token}`)
        }

        const response = await fetch(`${env.apiBaseUrl}${path}`, {
            method: 'GET',
            headers,
        })

        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`)
        }

        return response.json() as Promise<T>
    },
}