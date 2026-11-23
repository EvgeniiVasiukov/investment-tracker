import { useEffect, useState } from 'react'
import { ErrorState } from '@/components/states/ErrorState.tsx'
import {
    getPositions,
    type Position,
} from '@/features/portfolio/portfolioApi.ts'
import {
    Table,
    TableHeader,
    TableBody,
    TableRow,
    TableHead,
    TableCell
} from "@/components/ui/table.tsx";

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
        <section className="space-y-6">
            <div>
                <h1 className="text-2xl font-semibold tracking-tight">
                    Portfolio Overview
                </h1>
                <p className="text-sm text-muted-foreground">
                    View your current investment positions.
                </p>
            </div>

            <div className="overflow-hidden rounded-lg border bg-card">
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead>Ticker</TableHead>
                            <TableHead className="text-right">
                                Quantity
                            </TableHead>
                            <TableHead className="text-right">
                                Average Purchase Price
                            </TableHead>
                            <TableHead>Currency</TableHead>
                        </TableRow>
                    </TableHeader>

                    <TableBody>
                        {positions.map((position) => (
                            <TableRow key={position.id}>
                                <TableCell className="font-medium">
                                    {position.ticker}
                                </TableCell>
                                <TableCell className="text-right tabular-nums">
                                    {position.quantity}
                                </TableCell>
                                <TableCell className="text-right tabular-nums">
                                    {position.averagePrice.toFixed(2)}
                                </TableCell>
                                <TableCell>
                                    {position.currency}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </div>
        </section>
    )
}