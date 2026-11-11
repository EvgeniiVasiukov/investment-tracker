import type { ReactNode } from 'react'
import {
    Empty,
    EmptyHeader,
    EmptyTitle,
    EmptyDescription,
    EmptyMedia,
    EmptyContent,
} from '@/components/ui/empty'
import { Inbox } from 'lucide-react'

interface EmptyStateProps {
    title: string
    description?: string
    icon?: ReactNode
    action?: ReactNode
}

export function EmptyState({
                               title,
                               description,
                               icon = <Inbox />,
                               action,
                           }: EmptyStateProps) {
    return (
        <Empty>
            <EmptyHeader>
                <EmptyMedia variant="icon" aria-hidden="true">
                    {icon}
                </EmptyMedia>
                <EmptyTitle>{title}</EmptyTitle>
                {description && (
                    <EmptyDescription>{description}</EmptyDescription>
                )}
            </EmptyHeader>

            {action && <EmptyContent>{action}</EmptyContent>}
        </Empty>
    )
}