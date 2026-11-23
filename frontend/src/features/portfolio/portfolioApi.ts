import { httpClient } from '@/api/httpClient'

export interface Position {
    id: number
    userId: number
    ticker: string
    quantity: number
    averagePrice: number
    currency: string
    createdAt: string
}

export interface Page<T> {
    content: T[]
    number: number
    size: number
    totalElements: number
    totalPages: number
    first: boolean
    last: boolean
    empty: boolean
}

export function getPositions(): Promise<Page<Position>> {
    return httpClient.get<Page<Position>>('/positions')
}