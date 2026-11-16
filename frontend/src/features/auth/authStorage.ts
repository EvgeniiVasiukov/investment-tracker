import type { LoginResponse } from './types'
const AUTH_STORAGE_KEY = 'investment-tracker.auth'
export const authStorage = {
    save(session: LoginResponse): void {
        localStorage.setItem(
            AUTH_STORAGE_KEY,
            JSON.stringify(session)
        )
    },
    load(): LoginResponse | null {
        const stored = localStorage.getItem(AUTH_STORAGE_KEY)
        if (!stored) {
            return null
        }

        try {
            return JSON.parse(stored) as LoginResponse
        } catch {
            localStorage.removeItem(AUTH_STORAGE_KEY)
            return null
        }
    },
    clear(): void {
        localStorage.removeItem(AUTH_STORAGE_KEY)
    },
}