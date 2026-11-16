import { httpClient } from '../../api/httpClient'
import type { LoginRequest, LoginResponse } from './types'

export const authApi = {
    login(credentials: LoginRequest): Promise<LoginResponse> {
        return httpClient.post<LoginResponse>(
            '/auth/login',
            credentials,
            { skipAuth : true,
            })
    },
}