import { useEffect, useState } from 'react'
import type { ReactNode } from 'react'
import { AuthContext } from './authContext'
import { authStorage } from './authStorage'
import { authEvents } from './authEvents'
import type { LoginResponse } from './types'
interface AuthProviderProps {
    children: ReactNode
}
function getTokenExpiration(token: string): number | null {
    try {
        const payload = token.split('.')[1]
        if (!payload) {
            return null
        }

        const normalized = payload
            .replace(/-/g, '+')
            .replace(/_/g, '/')

        const decoded = JSON.parse(atob(normalized))
        const expiration = decoded.exp

        return typeof expiration === 'number' &&
        Number.isFinite(expiration)
            ? expiration * 1000
            : null
    } catch {
        return null
    }
}
function restoreSession(): LoginResponse | null {
    const session = authStorage.load()
    if (!session) {
        return null
    }

    const expiration = session.accessToken
        ? getTokenExpiration(session.accessToken)
        : null

    if (
        !session.accessToken ||
        !session.user ||
        expiration === null ||
        expiration <= Date.now()
    ) {
        authStorage.clear()
        return null
    }

    return session
}
export function AuthProvider({ children }: AuthProviderProps) {
    const [session, setSession] = useState<LoginResponse | null>(
        restoreSession
    )
    function login(newSession: LoginResponse) {
        authStorage.save(newSession)
        setSession(newSession)
    }

    function logout() {
        authStorage.clear()
        setSession(null)
    }

    useEffect(() => {
        const unsubscribe = authEvents.subscribe(() => {
            authStorage.clear()
            setSession(null)
        })

        return unsubscribe
    }, [])

    useEffect(() => {
        if (!session?.accessToken) {
            return
        }

        const expiration = getTokenExpiration(session.accessToken)

        const remainingTime = expiration === null
            ? 0
            : expiration - Date.now()

        const timeoutId = window.setTimeout(() => {
            authStorage.clear()
            setSession(null)
        }, Math.max(0, remainingTime))

        return () => window.clearTimeout(timeoutId)
    }, [session])

    return (
        <AuthContext.Provider
            value={{
                user: session?.user ?? null,
                accessToken: session?.accessToken ?? null,
                isAuthenticated: session !== null,
                login,
                logout,
            }}
        >
            {children}
            </AuthContext.Provider>
    )
}