import { createContext } from 'react'
import type { LoginResponse, UserDto } from './types'

export interface AuthContextValue {
    user: UserDto | null
    accessToken: string | null
    isAuthenticated: boolean
    login: (session: LoginResponse) => void
    logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)