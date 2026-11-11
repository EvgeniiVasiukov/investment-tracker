import { AlertCircle, RefreshCw } from 'lucide-react'
import {
    Alert,
    AlertTitle,
    AlertDescription,
} from '@/components/ui/alert'
import { Button } from '@/components/ui/button'

interface ErrorStateProps {
    title?: string
    message?: string
    onRetry?: () => void
    retryLabel?: string
}

export function ErrorState({
                               title = 'Unable to load data',
                               message = 'Something went wrong. Please try again later.',
                               onRetry,
                               retryLabel = 'Try again',
                           }: ErrorStateProps) {
    return (
        <Alert variant="destructive" role="alert">
            <AlertCircle aria-hidden="true" />

            <AlertTitle>{title}</AlertTitle>

            <AlertDescription>
                <p>{message}</p>

                {onRetry && (
                    <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        onClick={onRetry}
                        className="mt-3"
                    >
                        <RefreshCw aria-hidden="true" />
                        {retryLabel}
                    </Button>
                )}
            </AlertDescription>
        </Alert>
    )
}