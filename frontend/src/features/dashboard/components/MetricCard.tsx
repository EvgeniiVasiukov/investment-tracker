import {
    Card,
    CardContent,
    CardHeader,
    CardTitle,
} from '@/components/ui/card'
import { formatMoney } from '@/utils/formatMoney'
import type { Money } from '../dashboardApi'

interface MetricCardProps {
    title: string
    value: Money
}

export function MetricCard({ title, value }: MetricCardProps) {
    return (
        <Card>
            <CardHeader>
                <CardTitle className="text-sm font-medium text-muted-foreground">
                    {title}
                </CardTitle>
            </CardHeader>
            <CardContent>
                <p className="text-2xl font-bold">
                    {formatMoney(value.amount, value.currency)}
                </p>
            </CardContent>
        </Card>
    )
}