import { httpClient } from '@/api/httpClient'

export interface Money {
    amount: number
    currency: string | null
}

export interface Dashboard {
    totalPortfolioValue: Money
    totalInvestedAmount: Money
    unrealizedProfitLoss: Money
    remainingCreditBalance: Money
    netWorth: Money
    realizedProfitLoss: Money
    totalProfitLoss: Money
    calculatedAt: string
}

export function getDashboard(): Promise<Dashboard> {
    return httpClient.get<Dashboard>('/dashboard')
}