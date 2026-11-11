interface LoadingStateProps {
    message?: string
}

export function LoadingState({
                                 message = 'Loading data...',
                             }: LoadingStateProps) {
    return (
        <div role="status" aria-live="polite">
            <p>{message}</p>
        </div>
    )
}