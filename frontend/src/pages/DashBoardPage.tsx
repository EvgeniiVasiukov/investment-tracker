import { useEffect, useState } from 'react'
import {
    getDashboard,
    type Dashboard,
} from '@/features/dashboard/dashboardApi'
import { ErrorState } from '@/components/states/ErrorState'
import { MetricCard } from '@/features/dashboard/components/MetricCard'
import { Spinner } from '@/components/ui/spinner'

type DashboardState = {
    data: Dashboard | null
    loading: boolean
    error: boolean
}

export function DashBoardPage() {
    const [state, setState] = useState<DashboardState>({
        data: null,
        loading: true,
        error: false,
    })

    const [retryCount, setRetryCount] = useState(0)

    useEffect(() => {
        let active = true

        getDashboard()
            .then((data) => {
                if (active) {
                    setState({
                        data,
                        loading: false,
                        error: false,
                    })
                }
            })
            .catch(() => {
                if (active) {
                    setState({
                        data: null,
                        loading: false,
                        error: true,
                    })
                }
            })

        return () => {
            active = false
        }
    }, [retryCount])

    const handleRetry = () => {
        setState({
            data: null,
            loading: true,
            error: false,
        })

        setRetryCount((count) => count + 1)
    }

    if (state.loading) {
        return (
            <div className="flex min-h-[300px] flex-col items-center justify-center gap-3">
                <Spinner className="size-8" />
                <p className="text-sm text-muted-foreground">
                    Loading dashboard...
                </p>
            </div>
        )
    }

    if (state.error) {
        return (
            <ErrorState
                title="Unable to load dashboard"
                message="Please try again later."
                onRetry={handleRetry}
            />
        )
    }

    if (!state.data) {
        return (
            <p className="text-sm text-muted-foreground">
                No dashboard data available.
            </p>
        )
    }

    const dashboard = state.data

    return (
        <div className="space-y-6">
            <h1 className="text-2xl font-bold">
                Dashboard
            </h1>

            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                <MetricCard
                    title="Total Portfolio Value"
                    value={dashboard.totalPortfolioValue}
                />
                <MetricCard
                    title="Total Invested Amount"
                    value={dashboard.totalInvestedAmount}
                />
                <MetricCard
                    title="Net Worth"
                    value={dashboard.netWorth}
                />
                <MetricCard
                    title="Unrealized Profit/Loss"
                    value={dashboard.unrealizedProfitLoss}
                />
                <MetricCard
                    title="Realized Profit/Loss"
                    value={dashboard.realizedProfitLoss}
                />
                <MetricCard
                    title="Total Profit/Loss"
                    value={dashboard.totalProfitLoss}
                />
                <MetricCard
                    title="Remaining Credit Balance"
                    value={dashboard.remainingCreditBalance}
                />
            </div>

            <p className="text-sm text-muted-foreground">
                Last calculated:{' '}
                {new Date(dashboard.calculatedAt).toLocaleString()}
            </p>
        </div>
    )
}