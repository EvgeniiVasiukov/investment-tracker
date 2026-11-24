import { useEffect, useState } from 'react'
import {
    getDashboard,
    type Dashboard,
} from '@/features/dashboard/dashboardApi'
import { ErrorState } from '@/components/states/ErrorState'

export function DashBoardPage() {
    const [dashboard, setDashboard] = useState<Dashboard | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(false)
    const [retryCount, setRetryCount] = useState(0)

    useEffect(() => {
        let active = true

        setLoading(true)
        setError(false)

        getDashboard()
            .then((data) => {
                if (active) setDashboard(data)
            })
            .catch(() => {
                if (active) setError(true)
            })
            .finally(() => {
                if (active) setLoading(false)
            })

        return () => {
            active = false
        }
    }, [retryCount])

    if (loading) {
        return <p>Loading dashboard...</p>
    }

    if (error) {
        return (
            <ErrorState
                title="Unable to load dashboard"
                message="Please try again later."
                onRetry={() => setRetryCount((count) => count + 1)}
            />
        )
    }

    if (!dashboard) {
        return <p>No dashboard data available.</p>
    }

    return (
        <div>
            <h1>Dashboard</h1>
            <pre>{JSON.stringify(dashboard, null, 2)}</pre>
        </div>
    )
}