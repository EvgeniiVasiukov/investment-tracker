import { useState } from 'react'
import type { FormEvent } from 'react'
import { authApi } from '@/features/auth/authApi'
import { ApiError } from '@/api/ApiError'
import { Button } from '@/components/ui/button'
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Spinner } from '@/components/ui/spinner'
import  {useNavigate, useLocation} from "react-router";
import { useAuth} from "@/features/auth/useAuth.ts";


export function LoginPage() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [isLoading, setIsLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const navigate = useNavigate();
    const {login} = useAuth();
    const location = useLocation();
    const from = location.state?.from?.pathname ?? '/'
    async function handleSubmit(event: FormEvent<HTMLFormElement>) {
        event.preventDefault()
        if (isLoading) return

        setIsLoading(true)
        setError(null)

        try {
            const  session = await authApi.login({email, password})
            login(session)
            navigate(from, { replace: true})
        } catch (err) {
            if (err instanceof ApiError && err.status === 401) {
                setError('Invalid email or password.')
            } else {
                setError('Unable to sign in. Please try again.')
            }
        } finally {
            setIsLoading(false)
        }
    }
    return (
        <main className="flex min-h-screen items-center justify-center bg-background p-4">
            <Card className="w-full max-w-md">
                <CardHeader>
                    <CardTitle className="text-2xl">
                        Welcome back
                    </CardTitle>
                    <CardDescription>
                        Sign in to your Investment Tracker account
                    </CardDescription>
                </CardHeader>

                TypeScript
                <CardContent>
                <form onSubmit={handleSubmit} className="space-y-4">
                <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                    id="email"
                    name="email"
                    type="email"
                    placeholder="you@example.com"
                    autoComplete="email"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    disabled={isLoading}
                    required
                />
                </div>

                <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <Input
                    id="password"
                    name="password"
                    type="password"
                    placeholder="Enter your password"
                    autoComplete="current-password"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    disabled={isLoading}
                    required
                />
                </div>

                {error && (
                    <p role="alert" className="text-sm text-destructive">
                {error}
                </p>
                )}

                <Button
                    type="submit"
                    className="w-full"
                    disabled={isLoading}
                >
                    {isLoading ? (
                        <>
                            \<Spinner />
                            Signing in...
                        </>
                    ) : (
                        'Sign In'
                    )}
                    </Button>
                </form>
                </CardContent>
            </Card>
</main>
)
}