import { ErrorState } from '@/components/states/ErrorState.tsx'

export function PortfolioPage() {
    const handleRetry = () => {
        console.log('Retry clicked!')
    }

    return (
        <ErrorState
            title="Failed to load portfolio"
            message="We couldn't retrieve your investments. Please try again."
            onRetry={handleRetry}
        />
    )
}