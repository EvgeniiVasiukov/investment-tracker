import { useEffect, useState } from 'react'
import { ErrorState } from '@/components/states/ErrorState.tsx'
import {
    getPositions,
    type Position,
} from '@/features/portfolio/portfolioApi.ts'

export function PortfolioPage() {
    const [positions, setPositions] = useState<Position[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(false)
    const [retryCount, setRetryCount] = useState(0)

    useEffect(() => {
        let active = true

        getPositions()
            .then((page) => {
                if (active) {
                    setPositions(page.content)
                    setError(false)
                }
            })
            .catch(() => {
                if (active) {
                    setError(true)
                }
            })
            .finally(() => {
                if (active) {
                    setLoading(false)
                }
            })

        return () => {
            active = false
        }
    }, [retryCount])

    const handleRetry = () => {
        setLoading(true)
        setError(false)
        setRetryCount((count) => count + 1)
    }

    if (loading) {
        return <p>Loading portfolio...</p>
    }

    if (error) {
        return (
            <ErrorState
                title="Failed to load portfolio"
                message="We couldn't retrieve your investments. Please try again."
                onRetry={handleRetry}
            />
        )
    }

    if (positions.length === 0) {
        return <p>Your portfolio is empty.</p>
    }
    return (
        <section>
            <h1>Portfolio Overview</h1>
            <table>
            <thead>
        <tr>
            <th>Ticker</th>
            <th>Quantity</th>
            <th>Average Purchase Price</th>
            <th>Currency</th>
            </tr>
        </thead>
            <tbody>
        {positions.map((position) => (
            <tr key={position.id}>
        <td>{position.ticker}</td>
        <td>{position.quantity}</td>
        <td>{position.averagePrice.toFixed(2)}</td>
        <td>{position.currency}</td>
        </tr>
            ))}
            </tbody>
            </table>
</section>
)
}