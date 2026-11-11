import { Spinner } from '@/components/ui/spinner'
import './LoadingState.css'

interface LoadingStateProps {
    message?: string
}

export function LoadingState({
                                 message = 'Loading data...',
                             }: LoadingStateProps) {
    return (
        <div className="loading-state" role="status" aria-live="polite">
            <Spinner className="size-6 text-primary" aria-hidden="true" />
            <p className="loading-message">{message}</p>
        </div>
    )
}