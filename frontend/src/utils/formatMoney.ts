export function formatMoney(
    amount: number,
    currency: string | null
): string {
    if (!currency) {
        return new Intl.NumberFormat('en-US', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        }).format(amount)
    }

    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency,
    }).format(amount)
}